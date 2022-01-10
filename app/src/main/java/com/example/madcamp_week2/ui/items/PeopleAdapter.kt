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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.R
import org.json.JSONObject
import java.util.HashMap

class PeopleAdapter (val context: Context, val peopleInList: ArrayList<String>) :
    RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {
    private val baseURL = BASE_URL

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
                    .setMessage("빌리기 요청을 수락하시겠습니까?")
                    .setPositiveButton("확인") { dialog, which ->
                        Toast.makeText(itemView.context, "빌리기가 체결되었습니다.", Toast.LENGTH_SHORT).show()

                        //db에 계약 체결 보내기
//                        serverConfirmBorrow()
                   }
                    .setNegativeButton("취소") { dialog, which ->
                        Toast.makeText(itemView.context, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .create()

                alertDialog.show()
            }

        }

    }

//    private fun serverConfirmBorrow(from_user:String, to_user:String, item_id:String) {
//        val requestQueue = Volley.newRequestQueue(this)
//        Log.d("try", "server add item")
//        Log.d("try", "id:$user_id, id:$image, id:$name, id:$place, id:$price, id:$date_start, id:$date_end, id:$description")
//
//        val stringRequest = object : StringRequest(
//            Request.Method.POST, "$baseURL"+"/api/confirmBorrow",
//            Response.Listener<String> { res ->
//                val msg = JSONObject(res).getString("msg")
//                if (msg == "addItems successed") {
//                    Log.d("addItem", "res : $msg")
//
//                }
//            },
//            Response.ErrorListener { err ->
//                Log.d("addItem", "error! $err")
//            }) {
//            override fun getBodyContentType(): String {
//                return "application/json"
//            }
//            override fun getBody(): ByteArray {
//                val param = HashMap<String, String>()
//                param.put("user_id", user_id)
//                param.put("item_image", image)
//                param.put("item_name", name)
//                param.put("item_place", place)
//                param.put("item_date_start", date_start)
//                param.put("item_date_end", date_end)
//                param.put("item_price", price)
//                param.put("item_description", description)
//
//                return JSONObject(param as Map<*, *>).toString().toByteArray()
//            }
//        }
//        requestQueue.add(stringRequest)
//    }
}