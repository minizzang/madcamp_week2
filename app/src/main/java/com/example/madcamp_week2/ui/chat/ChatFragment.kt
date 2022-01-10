package com.example.madcamp_week2.ui.chat

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.R
import com.example.madcamp_week2.User
import com.example.madcamp_week2.databinding.FragmentChatBinding
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class ChatFragment : Fragment() {
    private val baseURL = BASE_URL

    private var _binding: FragmentChatBinding? = null
    lateinit var chatListView : RecyclerView
    private val dataArray = ArrayList<ChatListData>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        for(i: Int in 0..10)
//        {
//            dataArray.add(ChatListData("이름", "text text text"))
//        }

        //shared preference에서 user_id 가져오기
        val appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        var json = appSharedPreferences.getString("user", "")
        var obj = gson.fromJson(json, User::class.java)

        val user_id = obj.id.toString()

        serverGetUser1ChattingRoom(user_id)
        serverGetUser2ChattingRoom(user_id)

        chatListView = binding.chatListRV
        chatListView.layoutManager = LinearLayoutManager(requireContext())

        var adapter = ChatListViewAdapter(requireContext(), dataArray)
        chatListView.adapter = adapter
        chatListView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        /*adapter.setOnItemClickListener(object: ChatListViewAdapter.onItemClickListener{
            override fun onItemClick(v: View, data: ChatListData, pos: Int) {
                val intent = Intent(context, ChattingActivity::class.java)
                context?.startActivity(intent)
            }
        })*/

        return root
    }

    override fun onStart() {
        super.onStart()


    }

    // 내가 user1인 chatting room의 id와 상대 유저 nickname 가져오기
    private fun serverGetUser1ChattingRoom(user_id: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getUser1ChattingRoom/${user_id}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                for (i in 0 until resArrayLength) {
                    val resObj = JSONArray(res)[i].toString()
                    val room_id = JSONObject(resObj).getString("room_id")
                    val nickname = JSONObject(resObj).getString("nickname")

                    dataArray.add(ChatListData(nickname, room_id, user_id))
                    chatListView.adapter!!.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { err ->
                Log.d("getUser1ChattingRoom", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    // 내가 user2인 chatting room의 id와 상대 유저 nickname 가져오기
    private fun serverGetUser2ChattingRoom(user_id: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getUser2ChattingRoom/${user_id}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                for (i in 0 until resArrayLength) {
                    val resObj = JSONArray(res)[i].toString()
                    val room_id = JSONObject(resObj).getString("room_id")
                    val nickname = JSONObject(resObj).getString("nickname")

                    dataArray.add(ChatListData(nickname, room_id, user_id))
                    chatListView.adapter!!.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { err ->
                Log.d("getUser2ChattingRoom", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}