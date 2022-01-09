package com.example.madcamp_week2.ui.profile

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp_week2.LoginActivity
import com.example.madcamp_week2.MainActivity
import com.example.madcamp_week2.User
import com.example.madcamp_week2.databinding.FragmentProfileBinding
import com.google.gson.Gson
import com.nhn.android.naverlogin.OAuthLogin

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var mOAuthLoginInstance = OAuthLogin.getInstance()

        val appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
        val gson = Gson()
        var json = appSharedPreferences.getString("user", "")
        var obj = gson.fromJson(json, User::class.java)

        val nickNameView = binding.profileNickname
        val emailView = binding.profileEmail
        val nameView = binding.profileName
        val emailView2 = binding.profileEmail2
        val mobileView = binding.profileMobile
        val placeView = binding.profilePlace

        nickNameView.text = obj.nickname
        emailView.text = obj.email
        //nameView.text = obj.name
        emailView2.text = obj.email
        mobileView.text = obj.mobile
        placeView.text = obj.place


        binding.btnNaverLogout.setOnClickListener {
            mOAuthLoginInstance.logout(context)

            val appSharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
            val prefsEditor2 = appSharedPreferences2.edit()
            prefsEditor2.clear()
            prefsEditor2.commit()

            Log.d("logout", "successed")

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            //이전 버튼 누르면 다시 못 돌아오게 구현하기?
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}