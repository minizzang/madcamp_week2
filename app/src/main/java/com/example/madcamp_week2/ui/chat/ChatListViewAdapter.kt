package com.example.madcamp_week2.ui.chat

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R

class ChatListViewAdapter(val context: Context, val dataArray: ArrayList<ChatListData>): RecyclerView.Adapter<ChatListViewAdapter.ChatListViewHolder>() {

    //목록 item click 위한 listener interface
    /*interface onItemClickListener{
        fun onItemClick(v: View, data: ChatListData, pos: Int)
    }*/

    //my listener
    //private var myListener: onItemClickListener? = null

    /*fun setOnItemClickListener(listener: onItemClickListener){
        this.myListener = listener
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewAdapter.ChatListViewHolder {
        //return ChatListViewHolder(LayoutInflater.from(context).inflate(R.layout.chatting_list_item, parent, false), myListener)
        return ChatListViewHolder(LayoutInflater.from(context).inflate(R.layout.chatting_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val item = dataArray[position]
        holder.bind(item)
    }

    inner class ChatListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var holderName : TextView = itemView.findViewById(R.id.chatListName)
        var holderMessage : TextView = itemView.findViewById(R.id.chatListMessage)


        //binding data
        fun bind(dataArray: ChatListData){
            holderName.text = dataArray.name
            val room_id = dataArray.room_id
            val user_id = dataArray.user_id
//            holderMessage.text = dataArray.chatText

            itemView.setOnClickListener {
                val intent = Intent(context, ChattingActivity::class.java)

                intent.putExtra("room_id", room_id)
                intent.putExtra("user_id", user_id)  //현재 로그인한 계정 주인 id

                context.startActivity(intent)
            }

            /*val pos = adapterPosition

            if(pos!=RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener{
                    myListener?.onItemClick(itemView, dataArray, pos)
                }
            }*/
        }


    }
}