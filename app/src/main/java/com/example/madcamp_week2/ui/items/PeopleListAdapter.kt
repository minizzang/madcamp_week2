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

class PeopleListAdapter (val context: Context, val peopleListArray: ArrayList<RequestedItemList>) :
    RecyclerView.Adapter<PeopleListAdapter.PeopleListViewHolder>() {

    val viewPool : RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

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

        fun bindRequestItem(getList: RequestedItemList){
            hItemName.text = getList.itemName


            val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            layoutManager.recycleChildrenOnDetach = true

            val peopleAdapter = PeopleAdapter(context, getList.peopleList)
            Log.d("adapter", "dkfkd")
            hpersonList.layoutManager = layoutManager
            hpersonList.adapter = peopleAdapter
            hpersonList.adapter?.notifyDataSetChanged()
            hpersonList.setRecycledViewPool(viewPool)
        }

    }
}