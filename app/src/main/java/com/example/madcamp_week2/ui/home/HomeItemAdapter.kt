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
import kotlin.coroutines.coroutineContext


class HomeItemAdapter(private val context: Context) : RecyclerView.Adapter<HomeItemAdapter.ViewHolder>(){

    var items = ArrayList<ItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_item_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeItemAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun filterList(filteredList: ArrayList<ItemData>){
        items = filteredList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemName: TextView = itemView.findViewById(R.id.tv_itemName)
        private val itemDate: TextView = itemView.findViewById(R.id.tv_itemDate)
        private val itemPrice: TextView = itemView.findViewById(R.id.tv_itemPrice)

        fun bind(item : ItemData) {
            itemName.text = item.name
//            itemDate.text = item.date
            itemPrice.text = item.price.toString()

            itemView.setOnClickListener{
                val intent = Intent(context, ItemDetailActivity::class.java)
                context.startActivity(intent)
            }

        }
    }
}