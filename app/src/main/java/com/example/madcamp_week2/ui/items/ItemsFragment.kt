package com.example.madcamp_week2.ui.items

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.R
import com.example.madcamp_week2.User
import com.example.madcamp_week2.databinding.FragmentItemsBinding
import com.example.madcamp_week2.ui.home.ItemDetailActivity
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class ItemsFragment : Fragment() {
    private val baseURL = BASE_URL
    private var _binding: FragmentItemsBinding? = null
    lateinit var requestListView : RecyclerView //내가 신청한 물건 RV
    lateinit var peopleItemListView : RecyclerView //올린 아이템 가로 RV

    private val requestItemArray = ArrayList<ItemDataInList>() //신청 목록 list
    val peopleListArray = ArrayList<RequestedItemList>()
    val peopleArray = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentItemsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //shared preference에서 user_id 가져오기
        val appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        var json = appSharedPreferences.getString("user", "")
        var obj = gson.fromJson(json, User::class.java)

        val user_id = obj.id.toString()

        requestListView = binding.requestedList
        peopleItemListView = binding.requestPeople


        var adapterRequest = RequestItemAdapter(requireContext(), requestItemArray)

        requestListView.adapter = adapterRequest
        requestListView.adapter!!.notifyDataSetChanged()

        //var adapterPeopleList = PeopleListAdapter(requireContext(), peopleListArray, peopleArray)
        //peopleItemListView.adapter = adapterPeopleList
        //peopleItemListView.adapter!!.notifyDataSetChanged()


        serverGetBorrowReqItems(user_id)
        serverGetMyItemPosted(user_id)


        return root
    }

//    fun buildItemList() :ArrayList<RequestedItemList>  {
//        val peopleListArray = ArrayList<RequestedItemList>()
//        for(i : Int in 0..10){
//            val item = RequestedItemList("item", buildSub())
//            peopleListArray.add(item)
//        }
//
//        return peopleListArray
//    }
//
//    fun buildSub(): ArrayList<String> {
//        val peopleArray = ArrayList<String>()
//        peopleArray.add("minsu")
//
//        return peopleArray
//    }

    //내가 신청한 물건 빌려오기
    private fun serverGetBorrowReqItems(user_id: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getBorrowReqItems/${user_id}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                for (i in 0 until resArrayLength) {
                    val resObj = JSONArray(res)[i].toString()
                    val owner_id = JSONObject(resObj).getString("from_user")
                    val item_id = JSONObject(resObj).getString("contract_item")

                    serverGetItemDetail(item_id, owner_id)
                }
            },
            Response.ErrorListener { err ->
                Log.d("getBorrowReqItems", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    private fun serverGetItemDetail(itemId: String, ownerId: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getItemDetail/${itemId}",
            Response.Listener<String> { res ->
                val resObj = JSONArray(res)[0].toString()
                val item_name = JSONObject(resObj).getString("item_name")
                val item_date_start = JSONObject(resObj).getString("item_date_start")
                val item_date_end = JSONObject(resObj).getString("item_date_end")

                serverGetUserNickname(ownerId, item_name, item_date_start, item_date_end)
            },
            Response.ErrorListener { err ->
                Log.d("getItemDetail", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    private fun serverGetUserNickname(ownerId: String, itemName: String, dateStart: String, dateEnd: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getUserNickname/${ownerId}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resObj = JSONArray(res)[0].toString()
                val ownerNickName = JSONObject(resObj).getString("nickname")
                Log.d("borrowReq res", "$itemName, $ownerNickName, $dateStart, $dateEnd")
                requestItemArray.add(ItemDataInList(itemName, ownerNickName, "$dateStart~$dateEnd"))
                requestListView.adapter!!.notifyDataSetChanged()
            },
            Response.ErrorListener { err ->
                Log.d("GetUserNickname", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    // 나에게 신청한 사람 불러오기
    private fun serverGetMyItemPosted(user_id: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getMyItemPosted/${user_id}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                peopleListArray.clear()

                Log.d("length", peopleArray.size.toString())

                for (i in 0 until resArrayLength) {
                    peopleArray.clear()
                    val resObj = JSONArray(res)[i].toString()
                    val item_id = JSONObject(resObj).getString("item_id")
                    val item_name = JSONObject(resObj).getString("item_name")

                    serverGetPeopleReqItemToMe(user_id, item_id, item_name)

                    peopleListArray.add(RequestedItemList(item_name, peopleArray))


                }

//                var adapterPeopleList = PeopleListAdapter(requireContext(), peopleListArray, peopleArray)
//                peopleItemListView.adapter = adapterPeopleList
//
//                peopleItemListView.adapter?.notifyDataSetChanged()



                Log.d("length", peopleArray.size.toString())



            },
            Response.ErrorListener { err ->
                Log.d("GetMyItemPosted", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    private fun serverGetPeopleReqItemToMe(userId: String, itemId: String, itemName: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getPeopleReqItemToMe/${userId}/${itemId}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                Log.d("result", res)

                for (i in 0 until resArrayLength) {
                    val resObj = JSONArray(res)[i].toString()
                    val nickname = JSONObject(resObj).getString("nickname")

                    //serverGetUserNickname2(toUserId, itemName)
                    peopleArray.add(nickname)

                }
                Log.d("big list", "$itemName")





            },
            Response.ErrorListener { err ->
                Log.d("GetPeopleReqItemToMe", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    /*private fun serverGetUserNickname2(toUserId: String, itemName: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getUserNickname/${toUserId}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resObj = JSONArray(res)[0].toString()
                val ownerNickName = JSONObject(resObj).getString("nickname")
                Log.d("small list", "$itemName, $ownerNickName")


            },
            Response.ErrorListener { err ->
                Log.d("GetUserNickname", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }*/



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}