package com.example.madcamp_week2.ui.home

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
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var homeItemAdapter: HomeItemAdapter
    val items = mutableListOf<ItemData>()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecycler()

        val btn_add: View = binding.btnFloatingAdd
        btn_add.setOnClickListener{ view ->
            val intent = Intent(context, AddPostActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun initRecycler() {
        homeItemAdapter = HomeItemAdapter(requireContext())
        binding.mainRecycler.adapter = homeItemAdapter

        items.apply {
//            add(ItemData("충전기", "1월 6일", 500))
//            add(ItemData("충전기", "1월 6일", 500))
//            add(ItemData("충전기", "1월 6일", 0))
//            add(ItemData("충전기", "1월 6일", 0))
//            add(ItemData("충전기", "1월 6일", 500))
//            add(ItemData("충전기", "1월 6일", 500))


            homeItemAdapter.items = items
            homeItemAdapter.notifyDataSetChanged()
            binding.mainRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}