package com.example.madcamp_week2.ui.chat

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

class ChattingActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket
    private lateinit var username: String
    private lateinit var roomNum: String
    private lateinit var chattingView : RecyclerView

    var adapter = ChattingAdapter(this)

    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatting_screen)

        init()
        //var intent = intent
    }

    private fun init(){
        //connect to server
        try{
            mSocket = IO.socket("http://192.249.18.161:80")
            Log.d("SOCKET", "connection success: " + mSocket.id())
        }catch (e: URISyntaxException){
            e.printStackTrace()
        }

        val intent = intent
        //username = intent.getStringExtra("username").toString()
        //num = intent.getStringExtra("num").toString()

        //socket connection
        mSocket.connect()

        val obj = RoomData()
        username = "x3n7UQdmtP-5chFwbPNv6J2Xs-9I6yB9Ju-7RFc64Us"
        roomNum = "12"
        obj.setRoomData(username, roomNum)

        //send user and room info.
        mSocket.on(io.socket.client.Socket.EVENT_CONNECT){ args ->
            mSocket.emit("enter", gson.toJson(obj))
        }

        Log.d("SOCKET", "entered" + obj.getUsername())


        val contentView = findViewById<EditText>(R.id.inputMessage)
        val sendButton = findViewById<Button>(R.id.sendButton)

        //connect recycler view adapter
        chattingView = findViewById(R.id.chattingRV)
        chattingView.layoutManager = LinearLayoutManager(applicationContext)

        chattingView.adapter = adapter

        //메시지 보내기 실행 시
        sendButton.setOnClickListener() {

            Log.d("SOCKET", "click send")

            //message data 저장
            val message = MessageData()
            message.setMessageData("Message", username, roomNum, contentView.text.toString(), System.currentTimeMillis())

            fun sendMessage(){
                //새로운 message server에 보냄
                mSocket.emit("newMessage", gson.toJson(message))
                //recyclerview에서 표시하기 위해 chatting list에 추가
                adapter.addItem(ChatItem(username, contentView.text.toString(), toDate(System.currentTimeMillis()), ChatType.RIGHT_MESSAGE))
                chattingView.scrollToPosition(adapter.itemCount - 1)
                contentView.setText("")
            }

            sendMessage()
        }
        
        //상대가 새로운 message 보냈을 때 chatting list에 추가
        mSocket.on("update") { args ->
            val data: MessageData = gson.fromJson(args[0].toString(), MessageData::class.java)
            addChat(data)
        }
    }
    
    //새로운 chat 추가
    private fun addChat(data: MessageData) {
        runOnUiThread {
            val from = data.getFrom()
            val content = data.getContent()
            val time = toDate(data.getSendTime()!!)


            //adapter.addItem(ChatItem(from, content, time, ChatType.LEFT_MESSAGE))
            //chattingView.scrollToPosition(adapter.itemCount - 1)
            //상대가 보낸 message 추가
            if(username != data.getFrom()){
                adapter.addItem(ChatItem(from, content, time, ChatType.LEFT_MESSAGE))
                chattingView.scrollToPosition(adapter.itemCount - 1)
            }
            /*else{
                adapter.addItem(ChatItem(from, content, time, ChatType.LEFT_MESSAGE))
                chattingView.scrollToPosition(adapter.itemCount - 1)
            }*/
        }
    }

    //보낸 시간 format 설정
    private fun toDate(currentMiliis: Long) : String{
        return SimpleDateFormat("hh:mm a").format(Date(currentMiliis))
    }


    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
    }
}