package com.example.madcamp_week2.ui.items

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        for(i in 0 until peopleInList.size)
        {
            Log.d("check", peopleInList[i])
        }

        holder.bindPeople(item)
    }

    inner class PeopleViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var hNickName = itemView.findViewById<TextView>(R.id.requestNickname)
        var acceptbtn = itemView.findViewById<Button>(R.id.acceptBtn)

        fun bindPeople(peopleInList: String){
            hNickName.text = peopleInList

            acceptbtn.setOnClickListener {
                val alertDialog = AlertDialog.Builder(itemView.context)
                    .setMessage("진짜 수락하시겠습니까?")
                    .setPositiveButton("확인") { dialog, which ->
                        Toast.makeText(itemView.context, "빌리기가 체결되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("취소") { dialog, which ->
                        Toast.makeText(itemView.context, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .create()

                alertDialog.show()
            }

        }

    }
}