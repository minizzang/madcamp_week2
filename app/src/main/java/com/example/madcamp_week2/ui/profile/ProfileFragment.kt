package com.example.madcamp_week2.ui.profile

import android.content.Intent
import android.os.Bundle
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
import com.example.madcamp_week2.databinding.FragmentProfileBinding
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

        binding.btnNaverLogout.setOnClickListener {
            mOAuthLoginInstance.logout(context)
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