package com.example.madcamp_week2.ui.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.madcamp_week2.R

//채팅 내용 recyclerview adapter
class ChattingAdapter (var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var dataArray: ArrayList<ChatItem> = ArrayList()

    fun ChatAdapter(context: Context){
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        var view: View
        var context: Context = parent.context

        var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //var inflater = LayoutInflater.from(context)

        //view = inflater.inflate(R.layout.sent_message, parent, false)
        //return RightChattingViewHolder(view)

        //chat type에 따라 받은 message와 보낸 message 구분
        //다른 text bubble 이용
        if(viewType == ChatType.LEFT_MESSAGE){
            view = inflater.inflate(R.layout.recieve_message, parent, false)
            return LeftChattingViewHolder(view)
        }
        else{
            view = inflater.inflate(R.layout.sent_message, parent, false)
            return RightChattingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //val item: ChatItem = dataArray[position]
        //(holder as RightChattingViewHolder).setItemRight(item)
        if(dataArray[position].getViewType() == ChatType.LEFT_MESSAGE) {
            //Log.d("holder", "holder1" + dataArray.size)
            val item: ChatItem = dataArray[position]
            (holder as LeftChattingViewHolder).setItemLeft(item)
        }
        else if(dataArray[position].getViewType() == ChatType.RIGHT_MESSAGE) {
            //Log.d("holder", "holder2" + dataArray.size)
            val item: ChatItem = dataArray[position]
            (holder as RightChattingViewHolder).setItemRight(item)
        }
    }

    override fun getItemCount() : Int{
        return dataArray.size
    }

    fun addItem(item: ChatItem){
        dataArray.add(item)
        notifyDataSetChanged()
    }

    fun setItems(items: ArrayList<ChatItem>){
        this.dataArray = items
    }

    fun getItem(position: Int) : ChatItem{
        return dataArray[position]
    }

    override fun getItemViewType(position: Int) : Int{
        return dataArray[position].getViewType()
    }

    inner class LeftChattingViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val contentText: TextView = view.findViewById(R.id.recieveMessage)
        val sendTimeText: TextView = view.findViewById(R.id.textDataTimeR)

        fun setItemLeft(item: ChatItem){
            contentText.text = item.getContent()
            sendTimeText.text = item.getSendTime()
        }
    }

    inner class RightChattingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentText: TextView = view.findViewById(R.id.sentMessage)
        val sendTimeText: TextView = view.findViewById(R.id.textDataTimeS)


        fun setItemRight(item: ChatItem){
            contentText.text = item.getContent()
            sendTimeText.text = item.getSendTime()
        }
    }

}