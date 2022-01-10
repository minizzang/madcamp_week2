package com.example.madcamp_week2.ui.items

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

class ItemsFragment : Fragment() {
    private val baseURL = BASE_URL
    private var _binding: FragmentItemsBinding? = null
    lateinit var requestListView : RecyclerView //내가 신청한 물건 RV
    lateinit var peopleItemListView : RecyclerView //올린 아이템 가로 RV
    lateinit var peopleListView : RecyclerView //세로 RV

    private val requestItemArray = ArrayList<ItemDataInList>() //신청 목록 list
    val peopleListArray = ArrayList<RequestedItemList>()
    //val peopleArray = ArrayList<String>()

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
        peopleListView = inflater.inflate(R.layout.request_people_list, container, false).findViewById(R.id.peopleRV)


        var adapterRequest = RequestItemAdapter(requireContext(), requestItemArray)

        requestListView.adapter = adapterRequest
        requestListView.adapter!!.notifyDataSetChanged()



        serverGetBorrowReqItems(user_id)
        serverGetMyItemPosted(user_id)

        var peopleListAdapter = PeopleListAdapter(requireContext(), peopleListArray/*, peopleAdapter, peopleArray*/)
        var linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        peopleItemListView.layoutManager = linearLayoutManager
        peopleItemListView.adapter = peopleListAdapter
        peopleItemListView.adapter!!.notifyDataSetChanged()


        return root
    }


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

                fun dateFormat (input : String) : String{
                    var token = input.chunked(10)
                    return token[0].substring(2)
                }

                val resObj = JSONArray(res)[0].toString()
                val item_name = JSONObject(resObj).getString("item_name")
                val item_date_start = dateFormat(JSONObject(resObj).getString("item_date_start"))
                val item_date_end = dateFormat(JSONObject(resObj).getString("item_date_end"))

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


                for (i in 0 until resArrayLength) {

                    val resObj = JSONArray(res)[i].toString()
                    val item_id = JSONObject(resObj).getString("item_id")
                    val item_name = JSONObject(resObj).getString("item_name")


                    serverGetPeopleReqItemToMe(user_id, item_id, item_name)

                }

            },
            Response.ErrorListener { err ->
                Log.d("GetMyItemPosted", "error! $err")
            }){}

        requestQueue.add(stringRequest)


    }

    fun serverGetPeopleReqItemToMe(userId: String, itemId: String, itemName: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getPeopleReqItemToMe/${userId}/${itemId}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                Log.d("result", res)

                var peopleArray = ArrayList<String>()

                for (i in 0 until resArrayLength) {

                    val resObj = JSONArray(res)[i].toString()
                    val nickname = JSONObject(resObj).getString("nickname")

                    peopleArray.add(nickname)

                }

                Log.d("l", resArrayLength.toString())
                peopleListArray.add(RequestedItemList(itemName, peopleArray))
                peopleItemListView.adapter!!.notifyDataSetChanged()


                Log.d("people array", peopleArray.size.toString())
                Log.d("big list", "$itemName")

            },
            Response.ErrorListener { err ->
                Log.d("GetPeopleReqItemToMe", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}