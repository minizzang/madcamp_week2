package com.example.madcamp_week2.ui.items

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    //lateinit var peopleListView : RecyclerView //사람 목록 세로 RV

    private val requestItemArray = ArrayList<ItemDataInList>() //신청 목록 list
    //private val peopleListArray = ArrayList<RequestedItemList>() //올린 아이템과 목록 list에 대한 list
    //private val peopleArray = ArrayList<String>() //사람 목록 list

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

        serverGetBorrowReqItems(user_id)
        serverGetMyItemPosted(user_id)

        //임시 data
        for(i: Int in 0..10) {
            requestItemArray.add(ItemDataInList("키보드", "minsuh", "22-01-08 ~ 22-01-27"))
            //peopleArray.add("minsuh")
        }



        requestListView = binding.requestedList
        peopleItemListView = binding.requestPeople

        //val peopleView = layoutInflater.inflate(R.layout.request_people_list, null, false)
        //peopleListView = peopleView.findViewById(R.id.peopleRV)

        var adapterRequest = RequestItemAdapter(requireContext(), requestItemArray)
        //var adapterpeople = PeopleAdapter(requireContext(), peopleArray)
        var adapterPeopleList = PeopleListAdapter(requireContext(), buildItemList(), buildSub())

        requestListView.adapter = adapterRequest
        //peopleListView.adapter = adapterpeople
        peopleItemListView.adapter = adapterPeopleList

        return root
    }

    fun buildItemList() :ArrayList<RequestedItemList>  {
        val peopleListArray = ArrayList<RequestedItemList>()
        for(i : Int in 0..10){
            val item = RequestedItemList("item", buildSub())
            peopleListArray.add(item)
        }

        return peopleListArray
    }

    fun buildSub(): ArrayList<String> {
        val peopleArray = ArrayList<String>()
        peopleArray.add("minsu")

        return peopleArray
    }

    fun serverGetBorrowReqItems(user_id: String) {
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

//                    server
//                    requestItemArray.add(ItemDataInList("키보드", owner_nickname, "22-01-08 ~ 22-01-27"))
                }


                //shared preference에서 user_id 가져오기
                val appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                val gson = Gson()
                var json = appSharedPreferences.getString("user", "")
                var obj = gson.fromJson(json, User::class.java)

                val to_user_id = obj.id

                val intent = Intent(context, ItemDetailActivity::class.java)
//                intent.putExtra("item_id", item_id)
//                intent.putExtra("from_user", item_owner_id)
                intent.putExtra("to_user", to_user_id)

//                context.startActivity(intent)

            },
            Response.ErrorListener { err ->
                Log.d("getItemOwner", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    fun serverGetMyItemPosted(user_id: String) {
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

    private fun serverGetPeopleReqItemToMe(userId: String, itemId: String, itemName: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getMyItemPosted/${userId}/${itemId}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                for (i in 0 until resArrayLength) {
                    val resObj = JSONArray(res)[i].toString()
                    val toUserId = JSONObject(resObj).getString("to_user")

                    serverGetUserNickname(toUserId, itemName)
                }
            },
            Response.ErrorListener { err ->
                Log.d("GetPeopleReqItemToMe", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    private fun serverGetUserNickname(toUserId: String, itemName: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getMyItemPosted/${toUserId}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resObj = JSONArray(res)[0].toString()
                val toUserId = JSONObject(resObj).getString("nickname")

                // serverGetUserNickname(toUserId, itemName)
            },
            Response.ErrorListener { err ->
                Log.d("GetUserNickname", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}