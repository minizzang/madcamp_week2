package com.example.madcamp_week2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.MainActivity
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

        //검색창 위의 spinner
        var place : String = ""
        var place_data = listOf("서울", "대전", "대구", "부산", "충청", "제주")
        var adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, place_data)
        val spinner = binding.spinner2
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                place = place_data[p2]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

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
            add(ItemData("충전기", "1월 6일", 500))
            add(ItemData("충전기", "1월 6일", 500))
            add(ItemData("충전기", "1월 6일", 0))
            add(ItemData("충전기", "1월 6일", 0))
            add(ItemData("충전기", "1월 6일", 500))
            add(ItemData("충전기", "1월 6일", 500))


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