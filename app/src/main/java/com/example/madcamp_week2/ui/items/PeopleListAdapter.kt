package com.example.madcamp_week2.ui.items

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R

class PeopleListAdapter (val context: Context, val peopleListArray: ArrayList<RequestedItemList>, val peopleArray: ArrayList<String>) :
    RecyclerView.Adapter<PeopleListAdapter.PeopleListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PeopleListAdapter.PeopleListViewHolder{
        return PeopleListViewHolder(LayoutInflater.from(context).inflate(R.layout.request_people_list, parent, false))
    }

    override fun getItemCount(): Int {
        return peopleListArray.size
    }

    override fun onBindViewHolder(holder: PeopleListViewHolder, position: Int) {
        val item = peopleListArray[position]
        holder.bindRequestItem(item)
    }

    inner class PeopleListViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var hItemName = itemView.findViewById<TextView>(R.id.lendingItem)
        var hpersonList = itemView.findViewById<RecyclerView>(R.id.peopleRV)

        fun bindRequestItem(peopleList: RequestedItemList){
            hItemName.text = peopleList.itemName


            val layoutManager = LinearLayoutManager(hpersonList.context, LinearLayoutManager.VERTICAL, false)
            //layoutManager.initialPrefetchItemCount = item.peopleList.size

            val peopleAdapter = PeopleAdapter(context, peopleArray)
            hpersonList.layoutManager = layoutManager
            hpersonList.adapter = peopleAdapter

        }

    }
}