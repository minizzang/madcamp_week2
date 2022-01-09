package com.example.madcamp_week2.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.databinding.FragmentItemsBinding

class ItemsFragment : Fragment() {

    private var _binding: FragmentItemsBinding? = null
    lateinit var requestListView : RecyclerView //내가 신청한 물건 RV
    lateinit var peopleItemListView : RecyclerView //올린 아이템 가로 RV
    //lateinit var peopleListView : RecyclerView //사람 목록 세로 RV

    private val requestItemArray = ArrayList<ItemDataInList>() //신청 목록 list
    private val peopleListArray = ArrayList<RequestedItemList>() //올린 아이템과 목록 list에 대한 list
    private val peopleArray = ArrayList<String>() //사람 목록 list

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
        //임시 data
        for(i: Int in 0..10) {
            requestItemArray.add(ItemDataInList("키보드", "minsuh", "22-01-08 ~ 22-01-27"))
            //peopleArray.add("minsuh")
        }


        fun buildSub(): ArrayList<String> {
            //val list = ArrayList<String>()
            for(i : Int in 0..1) {
                peopleArray.add("minsuh")
            }

            return peopleArray
        }

        fun buildItemList() :ArrayList<RequestedItemList>  {
            //val list = ArrayList<RequestedItemList>()
            for(i : Int in 0..10){
                val item = RequestedItemList("item", buildSub())
                peopleListArray.add(item)
            }

            return peopleListArray
        }


        requestListView = binding.requestedList
        peopleItemListView = binding.requestPeople

        //val peopleView = layoutInflater.inflate(R.layout.request_people_list, null, false)
        //peopleListView = peopleView.findViewById(R.id.peopleRV)

        var adapterRequest = RequestItemAdapter(requireContext(), requestItemArray)
        //var adapterpeople = PeopleAdapter(requireContext(), peopleArray)
        var adapterPeopleList = PeopleListAdapter(requireContext(), buildItemList(), buildSub())

        requestListView.adapter = adapterRequest
        //peopleListView.adapter = adapterpeople
        peopleItemListView.adapter = adapterPeopleList

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}