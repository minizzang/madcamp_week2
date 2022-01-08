package com.example.madcamp_week2.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.databinding.FragmentItemsBinding

class ItemsFragment : Fragment() {

    private var _binding: FragmentItemsBinding? = null
    lateinit var lendListView : RecyclerView
    lateinit var borrowListView : RecyclerView
    lateinit var requestListView : RecyclerView

    private val lendItemArray = ArrayList<ItemDataInList>()
    private val borrowItemArray = ArrayList<ItemDataInList>()
    private val requestItemArray = ArrayList<ItemDataInList>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        for(i: Int in 0..10) {
            lendItemArray.add(ItemDataInList("충전기", "minsuh", "21-01-01 ~ 21-01-03"))
            borrowItemArray.add(ItemDataInList("명품백", "hello", "21-12-23 ~ 21-12-31"))
            requestItemArray.add(ItemDataInList("키보드", "minsuh", "22-01-08 ~ 22-01-27"))
        }

        lendListView = binding.lendList
        borrowListView = binding.borrowList
        requestListView = binding.requestedList

        //lendListView.layoutManager = LinearLayoutManager(requireContext())

        var adapterLend = LendItemAdapter(requireContext(), lendItemArray)
        var adapterBorrow = BorrowItemAdapter(requireContext(), borrowItemArray)
        var adapterRequest = RequestItemAdapter(requireContext(), requestItemArray)
        lendListView.adapter = adapterLend
        borrowListView.adapter = adapterBorrow
        requestListView.adapter = adapterRequest

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}