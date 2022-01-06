package com.example.madcamp_week2.ui.chat

import android.content.Context
import android.content.Intent
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
            holderMessage.text = dataArray.chatText

            itemView.setOnClickListener {
                val intent = Intent(context, ChattingActivity::class.java)
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