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

class NickNameActivity : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)

        var name:String = ""
        var nickname:String = ""
        var place:String = ""

        if (intent.hasExtra("user_name")) {
            name = intent.getStringExtra("user_name").toString()
            Log.d("name","$name")
        } else {
            Log.d("err", "none")
        }

        val userName = findViewById<TextView>(R.id.tv_userName)
        userName.setText(name)

        val txt_confirm_nickname = findViewById<TextView>(R.id.tv_confirm)

        val summitNickName:Button = findViewById<Button>(R.id.btn_summitNickName)
        summitNickName.setOnClickListener {

        //여기서 닉네임 중복확인
            nickname = summitNickName.text.toString()

            if (true) {     // 중복이 아니라면
                summitNickName.setBackgroundDrawable(resources.getDrawable(R.drawable.grey_square_round))
                summitNickName.setText("확인 완료")
                txt_confirm_nickname.setText("사용 가능한 별명입니다.")
                txt_confirm_nickname.setTextColor(Color.parseColor("#64C5F1"))  //반영 안됨???
                // 버튼 비활성화?
            }
            else {
                txt_confirm_nickname.setText("중복된 별명이 있습니다.")
                txt_confirm_nickname.setTextColor(Color.parseColor("#FF4537"))
            }
            // 8자 이상일 때 처리

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
            Log.d("place", "$place")
            Log.d("nickname", "$nickname")
            // 여기서 회원정보 db에 저장

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nickname", nickname)
            intent.putExtra("place", place)
            startActivity(intent)
            finish()
        }

    }
}