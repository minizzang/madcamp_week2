package com.example.madcamp_week2.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.R
import com.example.madcamp_week2.User
import com.example.madcamp_week2.ui.items.ItemDataInList
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class HistoryActivity : AppCompatActivity() {
    private val baseURL = BASE_URL
    lateinit var lendListView : RecyclerView
    lateinit var borrowListView : RecyclerView

    private val lendItemArray = ArrayList<ItemDataInList>()
    private val borrowItemArray = ArrayList<ItemDataInList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //shared preference에서 user_id 가져오기
        val appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val gson = Gson()
        var json = appSharedPreferences.getString("user", "")
        var obj = gson.fromJson(json, User::class.java)

        val user_id = obj.id.toString()

        serverGetMyItemHistory(user_id)
        serverGetMyBorrowHistory(user_id)

        lendListView = findViewById<RecyclerView>(R.id.lendList)
        borrowListView = findViewById<RecyclerView>(R.id.borrowList)

        var adapterLend = LendItemAdapter(this, lendItemArray)
        var adapterBorrow = BorrowItemAdapter(this, borrowItemArray)

        lendListView.adapter = adapterLend
        borrowListView.adapter = adapterBorrow

        val backBtn = findViewById<Button>(R.id.historyBack)

        backBtn.setOnClickListener {
            finish()
        }

    }

    //빌려준 물건
    private fun serverGetMyItemHistory(user_id: String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getMyItemHistory/${user_id}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                for (i in 0 until resArrayLength) {
                    val resObj = JSONArray(res)[i].toString()
                    val toUserId = JSONObject(resObj).getString("to_user")
                    val itemId = JSONObject(resObj).getString("contract_item")

                    serverGetItemDetail(toUserId, itemId, 0)
                }
            },
            Response.ErrorListener { err ->
                Log.d("getMyItemHistory", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    //빌린 물건
    private fun serverGetMyBorrowHistory(user_id: String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getMyBorrowHistory/${user_id}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                for (i in 0 until resArrayLength) {
                    val resObj = JSONArray(res)[i].toString()
                    val fromUserId = JSONObject(resObj).getString("from_user")
                    val itemId = JSONObject(resObj).getString("contract_item")

                    serverGetItemDetail(fromUserId, itemId, 1)
                }
            },
            Response.ErrorListener { err ->
                Log.d("getMyBorrowHistory", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    private fun serverGetItemDetail(userId: String, itemId: String, index: Int) {
        val requestQueue = Volley.newRequestQueue(this)
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

                serverGetUserNickname(userId, item_name, item_date_start, item_date_end, index)
            },
            Response.ErrorListener { err ->
                Log.d("getItemDetail", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    private fun serverGetUserNickname(ownerId: String, itemName: String, dateStart: String, dateEnd: String, index: Int) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getUserNickname/${ownerId}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resObj = JSONArray(res)[0].toString()
                val ownerNickName = JSONObject(resObj).getString("nickname")
                Log.d("etUserNickname!!", "$itemName, $ownerNickName, $dateStart, $dateEnd")

                if (index == 0){
                    lendItemArray.add(ItemDataInList(itemName, ownerNickName, "$dateStart~$dateEnd"))
                    lendListView.adapter!!.notifyDataSetChanged()
                } else {
                    borrowItemArray.add(ItemDataInList(itemName, ownerNickName, "$dateStart~$dateEnd"))
                    borrowListView.adapter!!.notifyDataSetChanged()
                }

            },
            Response.ErrorListener { err ->
                Log.d("GetUserNickname", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }
}