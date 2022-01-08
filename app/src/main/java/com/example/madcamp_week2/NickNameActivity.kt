package com.example.madcamp_week2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlin.properties.Delegates

class NickNameActivity : AppCompatActivity() {

    private val baseURL = BASE_URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)

        var id:String = ""
        var name:String = ""
        var email:String = ""
        var mobile:String = ""
        var nickname:String = ""
        var place:String = ""

        if (intent.hasExtra("user_id") && intent.hasExtra("user_name") && intent.hasExtra("user_email") && intent.hasExtra("user_mobile")) {
            id = intent.getStringExtra("user_id").toString()
            name = intent.getStringExtra("user_name").toString()
            email = intent.getStringExtra("user_email").toString()
            mobile = intent.getStringExtra("user_mobile").toString()
            Log.d("nameee","$name")
        } else {
            Log.d("err", "none")
        }

        val userName = findViewById<TextView>(R.id.tv_userName)
        userName.setText(name)

        val summitNickName:Button = findViewById<Button>(R.id.btn_summitNickName)
        summitNickName.setOnClickListener {
        //닉네임 중복확인
            nickname = findViewById<EditText>(R.id.editTextUserNickName).text.toString()
            serverCheckNickName(nickname)
        }

        var place_data = listOf("서울", "대전", "대구", "부산", "충청", "제주")
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, place_data)
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                place = place_data[p2]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // 회원가입 버튼 클릭 시
        val btn_signup = findViewById<Button>(R.id.btn_signup)
        btn_signup.setOnClickListener {
            // 회원정보 db에 저장
            serverAddUser(id, name, nickname, email, mobile, place)
        }
    }

    fun serverCheckNickName(nickname:String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, "$baseURL"+"/api/checkNickname",
            Response.Listener<String> { res ->
                val msg = JSONObject(res).getString("msg")
                if (msg == "duplicated") {
                    Log.d("checkNickname", "res : $msg")
                    handleNickname(true)
                } else {
                    handleNickname(false)
                }
            },
            Response.ErrorListener { err ->
                Log.d("checkNickname", "error! $err")
            }) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }
                override fun getBody(): ByteArray {
                    val param = HashMap<String, String>()
                    param.put("nickname", nickname)
                    return JSONObject(param as Map<*, *>).toString().toByteArray()
                }
            }
        requestQueue.add(stringRequest)
    }

    fun handleNickname(result:Boolean) {

        val txt_confirm_nickname = findViewById<TextView>(R.id.tv_confirm)
        val summitNickName:Button = findViewById<Button>(R.id.btn_summitNickName)

        if (!result) {     // 중복이 아니라면
//            summitNickName.setBackgroundDrawable(resources.getDrawable(R.drawable.grey_square_round))
//            summitNickName.setText("확인 완료")
            txt_confirm_nickname.setText("사용 가능한 별명입니다.")
            txt_confirm_nickname.setTextColor(Color.parseColor("#64C5F1"))  //반영 안됨???
            Log.d("dup test", "false")
            // 버튼 비활성화?
        }
        else {  // 닉네임 중복 처리
            txt_confirm_nickname.setText("중복된 별명이 있습니다.")
            txt_confirm_nickname.setTextColor(Color.parseColor("#FF4537"))
        }
        // 8자 이상일 때 처리
    }

    fun serverAddUser(id:String, name:String, nickname:String, email:String, mobile:String, place:String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, "$baseURL"+"/api/addUser",
            Response.Listener<String> { res ->
                val msg = JSONObject(res).getString("msg")
                if (msg == "Signup successed") {
                    Log.d("addUser", "res : $msg")

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("place", place)
                    startActivity(intent)
                    finish()
                }
            },
            Response.ErrorListener { err ->
                Log.d("checkNickname", "error! $err")
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getBody(): ByteArray {
                val param = HashMap<String, String>()
                        param.put("id", id)
                param.put("name", name)
                param.put("nickname", nickname)
                param.put("email", email)
                param.put("mobile", mobile)
                param.put("place", place)

                return JSONObject(param as Map<*, *>).toString().toByteArray()
            }
        }
        requestQueue.add(stringRequest)
    }
}