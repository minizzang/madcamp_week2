package com.example.madcamp_week2.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.R
import com.example.madcamp_week2.ui.chat.ChattingActivity
import org.json.JSONObject

class ItemDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val startChatting = findViewById<Button>(R.id.startChatting)

        startChatting.setOnClickListener() {
            val intent = Intent(this, ChattingActivity::class.java)
            this.startActivity(intent)
        }

        val borrowItem = findViewById<Button>(R.id.btn_borrow)

        borrowItem.setOnClickListener {
//            serverBorrowItem(from_user, to_user, contract_item)
        }
    }

//    fun serverBorrowItem(nickname:String) {
//        val requestQueue = Volley.newRequestQueue(this)
//        val stringRequest = object : StringRequest(
//            Request.Method.POST, "$baseURL"+"/api/checkNickname",
//            Response.Listener<String> { res ->
//                val msg = JSONObject(res).getString("msg")
//                if (msg == "duplicated") {
//                    Log.d("checkNickname", "res : $msg")
//                    handleNickname(true)
//                } else {
//                    handleNickname(false)
//                }
//            },
//            Response.ErrorListener { err ->
//                Log.d("checkNickname", "error! $err")
//            }) {
//            override fun getBodyContentType(): String {
//                return "application/json"
//            }
//            override fun getBody(): ByteArray {
//                val param = HashMap<String, String>()
//                param.put("nickname", nickname)
//                return JSONObject(param as Map<*, *>).toString().toByteArray()
//            }
//        }
//        requestQueue.add(stringRequest)
//    }

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