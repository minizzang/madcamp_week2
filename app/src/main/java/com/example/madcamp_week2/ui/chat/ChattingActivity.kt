package com.example.madcamp_week2.ui.chat

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.R
import com.example.madcamp_week2.ui.items.ItemDataInList
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChattingActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket
    private lateinit var userid: String
    private lateinit var roomNum: String
    private lateinit var chattingView : RecyclerView
    private val baseURL = BASE_URL

    var adapter = ChattingAdapter(this)

    var gson = Gson()

    //var getMessage = ArrayList<MessageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatting_screen)

        init()
        //var intent = intent

        val backBtn = findViewById<Button>(R.id.chattingBack)
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun init(){
        //connect to server
        try{
            mSocket = IO.socket(baseURL)
            Log.d("SOCKET", "connection success: " + mSocket.id())
        }catch (e: URISyntaxException){
            e.printStackTrace()
        }

        val intent = intent
        userid = intent.getStringExtra("user_id").toString()
        roomNum = intent.getStringExtra("room_id").toString()

        val nickName = intent.getStringExtra("nick_name").toString()
        val nickNameView = findViewById<TextView>(R.id.chattingName)

        nickNameView.text = nickName

        //socket connection
        mSocket.connect()

        val obj = RoomData()
//        username = "usernametest2"  // user_id
//        roomNum = "1"
        obj.setRoomData(userid, roomNum)

        //send user and room info.
        mSocket.on(io.socket.client.Socket.EVENT_CONNECT){ args ->
            mSocket.emit("enter", gson.toJson(obj))
        }

        Log.d("SOCKET", "entered" + obj.getUsername())
        serverGetChats(roomNum)

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
            val text = contentView.text.toString()
            message.setMessageData("Message", userid, roomNum, text, System.currentTimeMillis())
            //message data db에 저장
            serverSaveChat(roomNum, userid, text, toDate(System.currentTimeMillis()))

            fun sendMessage(){
                //새로운 message server에 보냄
                mSocket.emit("newMessage", gson.toJson(message))
                //recyclerview에서 표시하기 위해 chatting list에 추가
                adapter.addItem(ChatItem(userid, contentView.text.toString(), toDate(System.currentTimeMillis()), ChatType.RIGHT_MESSAGE))
                chattingView.scrollToPosition(adapter.itemCount - 1)
                contentView.setText("")
            }

            sendMessage()
        }
        
        //상대가 새로운 message 보냈을 때 chatting list에 추가
        mSocket.on("update") { args ->
            val data: MessageData = gson.fromJson(args[0].toString(), MessageData::class.java)
            //addChat(data)
        }
    }

    private fun serverSaveChat(roomNum: String, userid: String, text: String , timestamp: String) {
        val requestQueue = Volley.newRequestQueue(this)
        Log.d("saveChat", "id:$roomNum, id:$userid, id:$text")

        val stringRequest = object : StringRequest(
            Request.Method.POST, "$baseURL"+"/api/saveChat",
            Response.Listener<String> { res ->
                val msg = JSONObject(res).getString("msg")
                if (msg == "saveChat success") {
                    Log.d("saveChat", "res : $msg")
                }
            },
            Response.ErrorListener { err ->
                Log.d("saveChat", "error! $err")
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getBody(): ByteArray {
                val param = HashMap<String, String>()
                param.put("room_id", roomNum)
                param.put("from_user", userid)
                param.put("content", text)
                param.put("timestamp", timestamp)

                return JSONObject(param as Map<*, *>).toString().toByteArray()
            }
        }
        requestQueue.add(stringRequest)
    }

    //채팅방의 모든 chat 기록 불러오기
    private fun serverGetChats(roomId: String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getChats/${roomId}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                fun dateFormat (input : String) : String{
                    var token = input.chunked(10)
                    return token[0].substring(2)
                }

                for (i in 0 until resArrayLength) {
                    val resObj = JSONArray(res)[i].toString()
                    val message_id = JSONObject(resObj).getString("message_id")
                    val from_user = JSONObject(resObj).getString("from_user")
                    val content = JSONObject(resObj).getString("content")
                    val timeStamp = JSONObject(resObj).getString("timestamp")

                    //MessageData().setMessageData("Message", from_user, roomId, content, timeStamp)
                    if(from_user == userid) {
                        adapter.addItem(ChatItem(userid, content, timeStamp, ChatType.RIGHT_MESSAGE))
                    }
                    else {
                        adapter.addItem(ChatItem(from_user, content, timeStamp, ChatType.LEFT_MESSAGE))
                    }


                    Log.d("result", "$message_id, $from_user, $content, $timeStamp")
                }
            },
            Response.ErrorListener { err ->
                Log.d("GetChats", "error! $err")
            }){}

        requestQueue.add(stringRequest)
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
            if(userid != data.getFrom()){
                adapter.addItem(ChatItem(from, content, time, ChatType.LEFT_MESSAGE))
                chattingView.scrollToPosition(adapter.itemCount - 1)
            }
            else{
                adapter.addItem(ChatItem(from, content, time, ChatType.LEFT_MESSAGE))
                chattingView.scrollToPosition(adapter.itemCount - 1)
            }
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