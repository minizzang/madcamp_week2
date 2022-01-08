package com.example.madcamp_week2.ui.chat

import android.content.Intent
import android.os.Bundle
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
import com.example.madcamp_week2.R
import com.example.madcamp_week2.databinding.FragmentChatBinding

class ChatFragment : Fragment() {


    private var _binding: FragmentChatBinding? = null
    lateinit var chatListView : RecyclerView
    private val dataArray = ArrayList<ChatListData>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        for(i: Int in 0..10)
        {
            dataArray.add(ChatListData("이름", "text text text"))
        }


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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}