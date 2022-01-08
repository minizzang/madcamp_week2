package com.example.madcamp_week2.ui.chat

class MessageData {
    private lateinit var type: String
    private lateinit var from: String
    private lateinit var to: String
    private lateinit var content: String
    private var sendTime: Long? = null

    fun setMessageData(type: String, from: String, to: String, content: String, sendTime: Long){
        this.type = type
        this.from = from
        this.to = to
        this.content = content
        this.sendTime = sendTime
    }

    fun getType() : String{
        return  type
    }

    fun setType(type: String){
        this.type = type
    }

    fun getFrom() : String{
        return from
    }

    fun setFrom(from: String){
        this.from = from
    }

    fun getTo() : String{
        return  to
    }

    fun setTo(to: String){
        this.to = to
    }

    fun getContent() : String{
        return  content
    }

    fun setContent(content: String){
        this.content = content
    }

    fun getSendTime() : Long?{
        return sendTime
    }

    fun setSendTime(sendTime: Long){
        this.sendTime = sendTime
    }
}