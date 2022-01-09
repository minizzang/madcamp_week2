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
import com.example.madcamp_week2.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

            //intent로 받은 값 shared preference에 저장
            val userInfo = User()
            userInfo.id = intent.getStringExtra("user_id").toString()
            //userInfo.nickname = intent.getStringExtra("user_name").toString()
            //userInfo.place = intent.getStringExtra("place").toString()
            //userInfo.email = intent.getStringExtra("user_email").toString()
            //userInfo.mobile = intent.getStringExtra("user_mobile").toString()

            val appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.applicationContext)
            val prefsEditor = appSharedPrefs.edit()
            val gson = Gson()
            val json = gson.toJson(userInfo)

            prefsEditor.putString(userInfo.id, json)
            prefsEditor.commit()

            Log.d("PREFERENCE","saved id")

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
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //supportActionBar?.hide()
    }
}