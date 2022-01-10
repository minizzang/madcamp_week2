package com.example.madcamp_week2.ui.items

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.R
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject

class PeopleListAdapter (val context: Context, val peopleListArray: ArrayList<RequestedItemList>) :
    RecyclerView.Adapter<PeopleListAdapter.PeopleListViewHolder>() {

    val viewPool : RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PeopleListAdapter.PeopleListViewHolder{
        return PeopleListViewHolder(LayoutInflater.from(context).inflate(R.layout.request_people_list, parent, false))
    }

    override fun getItemCount(): Int {
        return peopleListArray.size
    }

    override fun onBindViewHolder(holder: PeopleListViewHolder, position: Int) {
        val item = peopleListArray[position]
        //Log.d("peopleListAdapter", peopleListArray.size.toString())
        Log.d("item", peopleListArray[position].peopleList.size.toString())
        holder.bindRequestItem(item)
    }

    inner class PeopleListViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var hItemName = itemView.findViewById<TextView>(R.id.lendingItem)
        var hpersonList = itemView.findViewById<RecyclerView>(R.id.peopleRV)

        fun bindRequestItem(getList: RequestedItemList){
            hItemName.text = getList.itemName


            val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            layoutManager.recycleChildrenOnDetach = true
            layoutManager.initialPrefetchItemCount = getList.peopleList.size
            layoutManager.isItemPrefetchEnabled = true

            Log.d("adapter", getList.peopleList.size.toString())
            var peopleAdapter = PeopleAdapter(itemView.context, getList.peopleList)
            //peopleListView.adapter = peopleAdapter
            hpersonList.layoutManager = layoutManager
            hpersonList.adapter = peopleAdapter
            hpersonList.adapter!!.notifyDataSetChanged()
            hpersonList.setRecycledViewPool(viewPool)
        }

    }


    /*fun serverGetPeopleReqItemToMe(userId: String, itemId: String, itemName: String) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest2 = object : StringRequest(
            Request.Method.GET, "$baseURL"+"/api/getPeopleReqItemToMe/${userId}/${itemId}",
            Response.Listener<String> { res ->
                val resArray = JSONArray(res)
                val resArrayLength :Int = resArray.length()

                Log.d("result", res)


                Log.d("l", resArrayLength.toString())

                for (i in 0 until resArrayLength) {

                    val resObj = JSONArray(res)[i].toString()
                    val nickname = JSONObject(resObj).getString("nickname")

                    //serverGetUserNickname2(toUserId, itemName)
                    peopleArray.add(nickname)

                }
                //peopleItemListView.adapter?.notifyDataSetChanged()
                //peopleArray.clear()

                Log.d("people array", peopleArray.size.toString())
                Log.d("big list", "$itemName")

            },
            Response.ErrorListener { err ->
                Log.d("GetPeopleReqItemToMe", "error! $err")
            }){}

        requestQueue.add(stringRequest2)
    }*/
}