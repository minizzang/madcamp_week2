package com.example.madcamp_week2.ui.home

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.R
import com.example.madcamp_week2.User
import com.example.madcamp_week2.ui.chat.ChattingActivity
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class ItemDetailActivity : AppCompatActivity() {
    lateinit var from_user:String   //빌려주는 사람
    lateinit var to_user:String     //빌리는 사람
    lateinit var item_id:String
    var room_id:String = ""
    private val baseURL = BASE_URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        item_id = intent.getStringExtra("item_id").toString()

        val startChatting = findViewById<Button>(R.id.startChatting)

        startChatting.setOnClickListener() {
            val intent = Intent(this, ChattingActivity::class.java)
            this.startActivity(intent)
        }

        serverGetItemDetail(item_id)


    }

    override fun onStart() {
        super.onStart()

        val borrowItem = findViewById<Button>(R.id.btn_borrow)

        borrowItem.setOnClickListener {
            serverBorrowItem(from_user, to_user, item_id)

            val alertDialog = AlertDialog.Builder(this)
                .setMessage("상대가 수락 시 빌리기가 체결됩니다.")
                .setPositiveButton("확인") { dialog, which ->
                    Toast.makeText(this, "신청되었습니다!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .create()

            alertDialog.show()
        }

        val startChatting = findViewById<Button>(R.id.startChatting)

        startChatting.setOnClickListener() {
            // db에 user1, user2 id 저장
            serverGetRoomNum(from_user, to_user)
        }

        val backButton = findViewById<Button>(R.id.itemDetailBack)

        backButton.setOnClickListener {
            finish()
        }
    }

    fun serverGetItemDetail(item_id :String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getItemDetail/${item_id}",
            Response.Listener<String> { res ->

                fun dateFormat (input : String) : String{
                    var token = input.chunked(10)
                    return token[0]
                }

                val resObj = JSONArray(res)[0].toString()
                val item_name = JSONObject(resObj).getString("item_name")
                val item_place = JSONObject(resObj).getString("item_place")
                val item_price = JSONObject(resObj).getString("item_price")
                val item_date_start = dateFormat(JSONObject(resObj).getString("item_date_start"))
                val item_date_end = dateFormat(JSONObject(resObj).getString("item_date_end"))
                val item_description = JSONObject(resObj).getString("item_description")

                findViewById<TextView>(R.id.ItemNameDetail).text = item_name
                findViewById<TextView>(R.id.ItemPlaceDetail).text = item_place
                findViewById<TextView>(R.id.ItemPriceDetail).text = item_price
                findViewById<TextView>(R.id.ItemDateStartDetail).text = item_date_start
                findViewById<TextView>(R.id.ItemDateEndDetail).text = item_date_end
                findViewById<TextView>(R.id.ItemDescriptionDetail).text = item_description

                //shared preference에서 user_id 가져오기
                val appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                val gson = Gson()
                var json = appSharedPreferences.getString("user", "")
                var obj = gson.fromJson(json, User::class.java)

                from_user = JSONObject(resObj).getString("user_id")
                to_user = obj.id.toString()
            },
            Response.ErrorListener { err ->
                Log.d("getItemDetail", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    fun serverBorrowItem(from_user:String, to_user:String, item_id:String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, "$baseURL"+"/api/borrowItem",
            Response.Listener<String> { res ->
                val msg = JSONObject(res).getString("msg")
                if (msg == "borrowItem request succeeded") {
                    Log.d("borrowItem", ": $msg")
                    // 빌리기 신청 완료
                    // 빌리기 버튼을 다른 모양으로?
                } else {
                    Log.d("borrowItem", ": $msg")
                }
            },
            Response.ErrorListener { err ->
                Log.d("borrowItem", "error! $err")
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getBody(): ByteArray {
                val param = HashMap<String, String>()
                param.put("from_user", from_user)
                param.put("to_user", to_user)
                param.put("contract_item", item_id)
                return JSONObject(param as Map<*, *>).toString().toByteArray()
            }
        }
        requestQueue.add(stringRequest)
    }


    fun serverGetRoomNum(user1_id:String, user2_id:String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, "$baseURL"+"/api/getRoomNum",
            Response.Listener<String> { res ->
                val msg = JSONObject(res).getString("msg")
                if (msg == "getRoomNum failed") {
                    Log.d("serverGetRoomNum", ": $msg")
                } else {
                    Log.d("roomNum", ": $msg")
                    room_id = msg

                    val intent = Intent(this, ChattingActivity::class.java)
                    intent.putExtra("room_id", room_id)
                    Log.d("room number", room_id)
                    intent.putExtra("user_id", to_user)  //현재 로그인한 계정 주인 id
                    this.startActivity(intent)

                }
            },
            Response.ErrorListener { err ->
                Log.d("getRoomNum", "error! $err")
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getBody(): ByteArray {
                val param = HashMap<String, String>()
                param.put("user1_id", user1_id)
                param.put("user2_id", user2_id)
                return JSONObject(param as Map<*, *>).toString().toByteArray()
            }
        }
        requestQueue.add(stringRequest)
    }

    // action bar의 back button 구현
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}