package com.example.madcamp_week2.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.ui.items.ItemDataInList

class LendItemAdapter (val context: Context, val itemArrayInList: ArrayList<ItemDataInList>) : RecyclerView.Adapter<LendItemAdapter.LendItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : LendItemViewHolder {
        return LendItemViewHolder(LayoutInflater.from(context).inflate(R.layout.history_item, parent, false))
    }

    override fun getItemCount(): Int {
        return itemArrayInList.size
    }

    override fun onBindViewHolder(holder: LendItemViewHolder, position: Int) {
        val item = itemArrayInList[position]
        holder.bindLendItem(item)
    }

    inner class LendItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var hItemName = itemView.findViewById<TextView>(R.id.itemName)
        var hNickName = itemView.findViewById<TextView>(R.id.personName)
        var hLendPeriod = itemView.findViewById<TextView>(R.id.Time)

        fun bindLendItem(itemInList: ItemDataInList){
            hItemName.text = itemInList.itemName
            hNickName.text = itemInList.nickName
            hLendPeriod.text = itemInList.lendPeriod

            itemView.setOnClickListener{

            }
        }

    }

}