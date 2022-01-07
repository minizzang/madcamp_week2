package com.example.madcamp_week2.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.madcamp_week2.R
import com.example.madcamp_week2.VolleyService
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.properties.Delegates

class AddPostActivity : AppCompatActivity() {
    lateinit var itemName: String
    lateinit var itemDate: String
    lateinit var itemPlace: String
    var itemPrice by Delegates.notNull<Int>()
    lateinit var itemDescription: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.btn_summit).setOnClickListener {
            itemName = R.id.edit_addItemName.toString()
            itemDate = R.id.edit_addItemDate.toString()
            itemPlace = R.id.edit_addItemPlace.toString()
            itemPrice = R.id.edit_addItemPrice
            itemDescription = R.id.edit_addItemDescription.toString()

//            Toast.makeText(this, "hi", Toast.LENGTH_LONG).show()
            Log.d("hihi", "clicked")
            VolleyService.testVolley(this) { testSuccess ->
                Log.d("hihi", "hihihihihi")
                if (testSuccess) {
                    Toast.makeText(this, "통신 성공!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "통신 실패ㅠ", Toast.LENGTH_LONG).show()
                }
            }


            postNewItem()
            finish()
        }
    }

    private fun postNewItem() {
        var th: Thread = object : Thread() {
            override fun run() {
                super.run()
                val jsonOb = JSONObject()
                jsonOb.put("user_name", "mini")
                jsonOb.put("item_name", itemName)
                jsonOb.put("item_price", itemPrice)

                val url = URL("https://172.10.5.90/postItem")
                var conn: HttpURLConnection? = null
                conn = url.openConnection() as HttpURLConnection
                conn.doOutput = true
                conn.requestMethod = "POST"
                conn.setRequestProperty("Connection", "Keep-Alive")
                conn.setRequestProperty("Content-Type", "application/json")

                val jsonStr = jsonOb.toString()
                val os: OutputStream = conn.outputStream
                os.write(jsonStr.toByteArray(charset("UTF-8")))
                os.flush()

                val sb = StringBuilder()
                val HttpResult = conn.responseCode
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    val br = BufferedReader(
                        InputStreamReader(conn.inputStream, "utf-8")
                    )
                    br.close()
                    println(""+sb.toString())
                }
                else
                    System.out.println(conn.responseMessage)
                os.close()
            }
        }
        th.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {       //상단탭의 뒤로가기 버튼
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
