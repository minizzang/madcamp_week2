package com.example.madcamp_week2.ui.items

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.R
import com.example.madcamp_week2.User
import com.google.gson.Gson
import org.json.JSONObject
import java.util.HashMap

class PeopleAdapter (val context: Context, val peopleInList: ArrayList<PeopleData>) :
    RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {
    private val baseURL = BASE_URL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PeopleAdapter.PeopleViewHolder {
        return PeopleViewHolder(LayoutInflater.from(context).inflate(R.layout.request_people_item, parent, false))
    }

    override fun getItemCount(): Int {
        return peopleInList.size
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val nickName = peopleInList[position].nickName
        val toUser = peopleInList[position].toUser
        val itemId = peopleInList[position].itemId
        holder.bindPeople(nickName, toUser, itemId)
    }

    inner class PeopleViewHolder(view: View) : RecyclerView.ViewHolder(view){
        //shared preference data 읽어오기
        val appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        var json = appSharedPrefs.getString("user", "")
        var obj = gson.fromJson(json, User::class.java)
        val user_id:String = obj.id.toString()

        var hNickName = itemView.findViewById<TextView>(R.id.requestNickname)
        var acceptbtn = itemView.findViewById<Button>(R.id.acceptBtn)

        fun bindPeople(nickName: String, toUser: String, itemId: String){
            hNickName.text = nickName

            acceptbtn.setOnClickListener {
                val alertDialog = AlertDialog.Builder(itemView.context)
                    .setMessage("빌리기 요청을 수락하시겠습니까?")
                    .setPositiveButton("확인") { dialog, which ->
                        Toast.makeText(itemView.context, "빌리기가 체결되었습니다.", Toast.LENGTH_SHORT).show()

                        //db에 계약 체결 보내기
                        serverConfirmBorrow(user_id, toUser, itemId)
                   }
                    .setNegativeButton("취소") { dialog, which ->
                        Toast.makeText(itemView.context, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .create()

                alertDialog.show()
            }

        }

    }

    private fun serverConfirmBorrow(from_user:String, to_user:String, item_id:String) {
        val requestQueue = Volley.newRequestQueue(context)

        Log.d("serverConfirmBorrow", "from_user:$from_user, to_user:$to_user, item_id:$item_id")

        val stringRequest = object : StringRequest(
            Request.Method.POST, "$baseURL"+"/api/confirmBorrow",
            Response.Listener<String> { res ->
                val msg = JSONObject(res).getString("msg")
                if (msg == "confirmBorrow Succeeded") {
                    Log.d("confirmBorrow", "res : $msg")
                }
            },
            Response.ErrorListener { err ->
                Log.d("confirmBorrow", "error! $err")
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getBody(): ByteArray {
                val param = HashMap<String, String>()
                param.put("from_user", from_user)
                param.put("to_user", to_user)
                param.put("item_id", item_id)

                return JSONObject(param as Map<*, *>).toString().toByteArray()
            }
        }
        requestQueue.add(stringRequest)
    }
}