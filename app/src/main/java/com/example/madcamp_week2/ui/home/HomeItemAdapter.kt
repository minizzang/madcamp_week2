package com.example.madcamp_week2.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.coroutineContext


class HomeItemAdapter(private val context: Context) : RecyclerView.Adapter<HomeItemAdapter.ViewHolder>(){

    var items = ArrayList<ItemData>()
    private val baseURL = BASE_URL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_item_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeItemAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemName: TextView = itemView.findViewById(R.id.tv_itemName)
        private val itemDateStart: TextView = itemView.findViewById(R.id.tv_itemDateStartShow)
        private val itemDateEnd: TextView = itemView.findViewById(R.id.tv_itemDateEndShow)
        private val itemPrice: TextView = itemView.findViewById(R.id.tv_itemPriceShow)

        fun bind(item : ItemData) {
            itemName.text = item.name
            itemDateStart.text = item.date_start+" ~ "
            itemDateEnd.text = item.date_end
            itemPrice.text = item.price.toString()+"원"

            itemView.setOnClickListener{

                //item_id를 넣으면 어떤 유저의 item인지 리턴
                serverGetItemOwner(item.item_id)
            }

        }
    }

    fun serverGetItemOwner(item_id :String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getItemOwner/${item_id}",
            Response.Listener<String> { res ->
                val item_owner_id = JSONObject(res).getString("user_id")

                //shared preference에서 user_id 가져오기
//                val to_user_id =

                val intent = Intent(context, ItemDetailActivity::class.java)
                intent.putExtra("item_id", item_id)
                intent.putExtra("from_user", item_owner_id)
//                intent.putExtra("to_user", to_user_id)

                context.startActivity(intent)

            },
            Response.ErrorListener { err ->
                Log.d("getItemOwner", "error! $err")
            }){}

        requestQueue.add(stringRequest)
    }
}