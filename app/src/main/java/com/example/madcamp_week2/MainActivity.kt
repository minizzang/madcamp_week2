package com.example.madcamp_week2

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Window
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.databinding.ActivityMainBinding
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val baseURL = BASE_URL
    private lateinit var user_id:String
    private lateinit var user_name:String
    private lateinit var user_nickname:String
    private lateinit var user_email:String
    private lateinit var user_mobile:String
    private lateinit var user_place:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        var name:String;
        if (intent.hasExtra("user_name")) {
            name = intent.getStringExtra("user_name").toString()
            Log.d("name","$name")

            //intent로 받은 값 db에 저장
            user_id = intent.getStringExtra("user_id").toString()
            serverFindUser(user_id)

        } else {
            Log.d("err", "none")
        }



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_chat, R.id.navigation_items, R.id.navigation_profile
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //supportActionBar?.hide()
    }

    fun serverFindUser(id:String) {

        Log.d("id", "$id")
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Request.Method.POST, "$baseURL"+"/api/findUser",
            Response.Listener<String> { res ->
                Log.d("msg", "$res")
                val msg :String = JSONArray(res)[0].toString();
                if (JSONObject(msg).getString("id")!="empty_user") {   // 무조건 여기 걸려야 함.
                    val userObj = JSONObject(msg)
                    user_name = userObj.getString("name")
                    user_nickname = userObj.getString("nickname")
                    user_email = userObj.getString("email")
                    user_mobile = userObj.getString("mobile")
                    user_place = userObj.getString("place")

                    // 유저 정보를 shared preference에 저장
                    storeUserInfo()
                } else {
                    Log.d("main_findUser", "error")
                }
            },
            Response.ErrorListener { err ->
                Log.d("main_findUser", "error! $err")
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getBody(): ByteArray {
                val param = HashMap<String, String>()
                param.put("id", id)
                return JSONObject(param as Map<*, *>).toString().toByteArray()
            }
        }

        requestQueue.add(stringRequest)
    }

    private fun storeUserInfo() {
        val userInfo = User()
        userInfo.id = user_id
        userInfo.name = user_name
        userInfo.nickname = user_nickname
        userInfo.place = user_place
        userInfo.email = user_email
        userInfo.mobile = user_mobile

        val appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.applicationContext)
        val prefsEditor = appSharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(userInfo)

        prefsEditor.putString("user", json)
        prefsEditor.commit()

        Log.d("PREFERENCE","saved id")
    }
}