package com.example.madcamp_week2.ui.home

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
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
    var place : String = "seoul"    // 나중에 user의 place를 default로 설정
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //검색창 위의 spinner

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

        //initRecycler()

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

        val priceButton = binding.priceFilter
        val periodButton = binding.dateFilter
        //var priceFilter = -1
        //val dateFilter = -1

        priceButton.setOnClickListener {
            val priceSheet = BottomSheetPrice()
            priceSheet.show(parentFragmentManager, priceSheet.tag )

            /*val priceSheetView = layoutInflater.inflate(R.layout.search_filter_price, null, false)
            val belowTen = priceSheetView.findViewById<Button>(R.id.belowTen)
            val tenToTwenty = priceSheetView.findViewById<Button>(R.id.tenToTewenty)
            val twentyToThirty = priceSheetView.findViewById<Button>(R.id.twentyToThirty)
            val thirtyToFourty = priceSheetView.findViewById<Button>(R.id.thirtyToFourty)
            val fourtyToFifty = priceSheetView.findViewById<Button>(R.id.fourtyToFifty)
            val aboveFifty = priceSheetView.findViewById<Button>(R.id.aboveFifty)

            var btnArray = arrayOf(belowTen, tenToTwenty, twentyToThirty, thirtyToFourty, fourtyToFifty, aboveFifty)

            belowTen.setOnClickListener {
                priceFilter = 1
                Log.d("button", "$priceFilter")
            }*/
        }

        periodButton.setOnClickListener {
            val periodSheet = BottomSheetPeriod()
            periodSheet.show(parentFragmentManager, periodSheet.tag )
        }



        Log.d("place1", "$place")
        serverGetItems(place/*, priceFilter*/)

    }

    fun serverGetItems(place:String/*, priceFilter:Int*/) {
        Log.d("place", "$place")
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getItems/${place}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()
                var items = ArrayList<ItemData>()

                items.apply {
                    for (i in 0 until resArrayLength) {
                        val resMsg :String = JSONArray(res)[i].toString();

                        val item_id = JSONObject(resMsg).getString("item_id")
                        val item_image = JSONObject(resMsg).getString("item_image")
                        val item_name = JSONObject(resMsg).getString("item_name")
                        val item_post_time = JSONObject(resMsg).getString("post_time")
                        val item_date_start = JSONObject(resMsg).getString("item_date_start")
                        val item_date_end = JSONObject(resMsg).getString("item_date_end")
                        val item_price = JSONObject(resMsg).getString("item_price").toInt()
                        val item_place = JSONObject(resMsg).getString("item_place")

                        //if(priceFilter == -1) {
                            add(ItemData(item_id, item_image, item_name, item_post_time, item_date_start, item_date_end, item_price, item_place))
                        //}


                    }
                        homeItemAdapter.items = items
                        homeItemAdapter.notifyDataSetChanged()
                        
                        //아이템 이름으로 검색
                        val searchET = binding.searchText
                        val filteredList = ArrayList<ItemData>()

                        searchET.addTextChangedListener(object : TextWatcher{

                            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                            }

                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                val searchText = searchET.text.toString()
                                searchFilter(searchText)
                            }

                            override fun afterTextChanged(p0: Editable?) {

                            }

                            fun searchFilter(searchText: String){
                                filteredList.clear()

                                for(i in 0 until items.size){
                                    if(items[i].name.contains(searchText)){
                                        filteredList.add(items[i])
                                    }
                                }

                                homeItemAdapter.filterList(filteredList)
                            }
                    })

                    fun onResume(){
                        super.onResume()
                    }

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