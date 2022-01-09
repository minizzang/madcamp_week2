package com.example.madcamp_week2.ui.items

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R

class PeopleAdapter (val context: Context, val peopleInList: ArrayList<String>) :
    RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PeopleAdapter.PeopleViewHolder {
        return PeopleViewHolder(LayoutInflater.from(context).inflate(R.layout.request_people_item, parent, false))
    }

    override fun getItemCount(): Int {
        return peopleInList.size
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val item = peopleInList[position]
        holder.bindPeople(item)
    }

    inner class PeopleViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var hNickName = itemView.findViewById<TextView>(R.id.requestNickname)

        fun bindPeople(peopleInList: String){
            hNickName.text = peopleInList
        }

    }
}