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
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.R
import com.example.madcamp_week2.ui.chat.ChattingActivity
import org.json.JSONObject

class ItemDetailActivity : AppCompatActivity() {
    lateinit var from_user:String   //빌려주는 사람
    lateinit var to_user:String     //빌리는 사람
    lateinit var item_id:String
    private val baseURL = BASE_URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        from_user = intent.getStringExtra("from_user").toString()
        to_user = intent.getStringExtra("to_user").toString()
        item_id = intent.getStringExtra("item_id").toString()

        val startChatting = findViewById<Button>(R.id.startChatting)

        startChatting.setOnClickListener() {
            val intent = Intent(this, ChattingActivity::class.java)
            this.startActivity(intent)
        }

        val borrowItem = findViewById<Button>(R.id.btn_borrow)

        borrowItem.setOnClickListener {
            serverBorrowItem(from_user, to_user, item_id)
        }
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