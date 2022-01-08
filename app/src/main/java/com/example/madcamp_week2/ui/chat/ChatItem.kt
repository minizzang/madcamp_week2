package com.example.madcamp_week2.ui.chat

class ChatItem(from: String, content: String, time: String, i: Int) {
    private var name: String = from
    private var content: String = content
    private var sendTime: String = time
    // 0 -> 상대, 1->입장, 2->나
    private var viewType: Int = i

    /*fun setChatItem(name: String, content: String, sendTime: String, viewType: Int){
        this.name = name
        this.content = content
        this.sendTime = sendTime
        this.viewType = viewType
    }*/

    fun getName() : String{
        return name
    }

    fun setName(name: String){
        this.name = name
    }

    fun getContent() : String{
        return content
    }

    fun setContent(content: String){
        this.content = content
    }

    fun getSendTime() : String{
        return sendTime
    }

    fun setSendTime(sendTime: String){
        this.sendTime = sendTime
    }

    fun getViewType() : Int{
        return viewType
    }

    fun setViewType(viewType: Int){
        this.viewType = viewType
    }

}