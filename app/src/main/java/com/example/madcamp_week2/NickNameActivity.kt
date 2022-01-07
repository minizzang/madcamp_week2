package com.example.madcamp_week2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class NickNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)

        var name:String = ""
        if (intent.hasExtra("user_name")) {
            name = intent.getStringExtra("user_name").toString()
            Log.d("name","$name")
        } else {
            Log.d("err", "none")
        }

        val userName = findViewById<TextView>(R.id.tv_userName)
        userName.setText(name)

        val summitNickName = findViewById<Button>(R.id.btn_summitNickName)
        summitNickName.setOnClickListener {
            val user_nickname = summitNickName.text

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user_nickname", user_nickname)
            startActivity(intent)
            finish()
        }
    }
}