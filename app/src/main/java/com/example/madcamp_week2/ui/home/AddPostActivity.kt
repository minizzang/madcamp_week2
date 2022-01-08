package com.example.madcamp_week2.ui.home

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.R
import com.example.madcamp_week2.VolleyService
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.properties.Delegates
import com.bumptech.glide.Glide
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.MainActivity
import java.io.ByteArrayOutputStream
import java.lang.Exception

class AddPostActivity : AppCompatActivity() {
    lateinit var itemName: String
    lateinit var itemDateStart : String
    lateinit var itemDateEnd : String
    lateinit var itemPlace: String
    lateinit var itemPrice: String
    lateinit var itemImage: String
    lateinit var itemDescription: String
    private val GALLERY = 1
    private val baseURL = BASE_URL

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val btn_image = findViewById<ImageButton>(R.id.btn_image)
        btn_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, GALLERY)
        }

        val btn_dateStart : Button = findViewById<Button>(R.id.btn_addItemDateStart)
        btn_dateStart.setOnClickListener{
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    btn_dateStart.text = "${year}-${month+1}-${dayOfMonth}"
                }
            }, year, month, date)

            dlg.show()
        }

        val btn_dateEnd : Button = findViewById<Button>(R.id.btn_addItemDateEnd)
        btn_dateEnd.setOnClickListener{
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    btn_dateEnd.text = "${year}-${month+1}-${dayOfMonth}"
                }
            }, year, month, date)

            dlg.show()
        }

        // 등록하기 버튼을 눌렀을 때
        findViewById<Button>(R.id.btn_summit).setOnClickListener {
            itemName = R.id.edit_addItemName.toString()
            itemPlace = R.id.edit_addItemPlace.toString()
            itemPrice = R.id.edit_addItemPrice.toString()
            itemDateStart = btn_dateStart.text.toString()
            itemDateEnd = btn_dateEnd.text.toString()
            itemDescription = R.id.edit_addItemDescription.toString()

            val drawable = findViewById<ImageView>(R.id.iv_itemImage).drawable
            val bitmapDrawable = drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            //bitmap to string
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val bytes = stream.toByteArray()
            itemImage = java.util.Base64.getEncoder().encodeToString(bytes)

            // db에 아이템 추가
            serverAddItem(itemImage, itemName, itemPlace, itemPrice, itemDateStart, itemDateEnd, itemDescription)
        }
    }

    fun serverAddItem(image:String, name:String, place:String, price:String, date_start:String, date_end:String, description:String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, "$baseURL"+"/api/addItem",
            Response.Listener<String> { res ->
                val msg = JSONObject(res).getString("msg")
                if (msg == "addItems successed") {
                    Log.d("addItem", "res : $msg")

                    finish()
                }
            },
            Response.ErrorListener { err ->
                Log.d("addItem", "error! $err")
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getBody(): ByteArray {
                val param = HashMap<String, String>()
//                param.put("user_id", )
                param.put("item_image", image)
                param.put("item_name", name)
                param.put("item_place", place)
                param.put("item_date_start", date_start)
                param.put("item_date_end", date_end)
                param.put("item_price", price)
                param.put("item_description", description)

                return JSONObject(param as Map<*, *>).toString().toByteArray()
            }
        }
        requestQueue.add(stringRequest)
    }

    //갤러리에서 사진 불러오고 띄우기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val tempView = findViewById<ImageView>(R.id.iv_itemImage)
        val imageLoader = findViewById<ImageButton>(R.id.btn_image)

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY) {
                var ImageData: Uri? = data?.data
                try {
                    imageLoader.visibility = View.INVISIBLE
                    tempView.visibility = View.VISIBLE
                    Glide.with(this).load(ImageData).into(tempView)
                } catch (e:Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    //상단탭의 뒤로가기 버튼
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
