package com.example.madcamp_week2.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.MainActivity
import com.example.madcamp_week2.R
import com.example.madcamp_week2.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment() {
    private val baseURL = BASE_URL
    lateinit var homeItemAdapter: HomeItemAdapter

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
        var place_data_eng = listOf("seoul", "daejeon", "daegu", "busan", "chungcheong", "jeju")
        var adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, place_data)
        val spinner = binding.spinner2
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                place = place_data_eng[p2]
                initRecycler()
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

        val priceButton = binding.priceFilter
        val periodButton = binding.dateFilter

        priceButton.setOnClickListener {
            val priceSheet = BottomSheetPrice()
            priceSheet.show(parentFragmentManager, priceSheet.tag )
        }

        periodButton.setOnClickListener {
            val periodSheet = BottomSheetPeriod()
            periodSheet.show(parentFragmentManager, periodSheet.tag )
        }

        return root
    }

    private fun initRecycler() {
        homeItemAdapter = HomeItemAdapter(requireContext())
        binding.mainRecycler.adapter = homeItemAdapter

        Log.d("place1", "$place")
        serverGetItems(place)

    }

    fun serverGetItems(place:String) {
        Log.d("place", "$place")
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getItems/${place}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()
                val items = mutableListOf<ItemData>()

                items.apply {
                    for (i in 0 until resArrayLength) {
                        val resMsg :String = JSONArray(res)[i].toString();

                        val item_id = JSONObject(resMsg).getString("item_id")
                        val item_name = JSONObject(resMsg).getString("item_name")
                        val item_post_time = JSONObject(resMsg).getString("post_time")
                        val item_date_start = JSONObject(resMsg).getString("item_date_start")
                        val item_date_end = JSONObject(resMsg).getString("item_date_end")
                        val item_price = JSONObject(resMsg).getString("item_price").toInt()
                        val item_place = JSONObject(resMsg).getString("item_place")

                        add(ItemData(item_id, item_name, item_post_time, item_date_start, item_date_end, item_price, item_place))
                    }
                        homeItemAdapter.items = items
                        homeItemAdapter.notifyDataSetChanged()
                        binding.mainRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                    }

            },
            Response.ErrorListener { err ->
                Log.d("getItems", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}