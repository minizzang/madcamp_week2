package com.example.madcamp_week2.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcamp_week2.BASE_URL
import com.example.madcamp_week2.User
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.coroutineContext
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.graphics.drawable.BitmapDrawable


class HomeItemAdapter(private val context: Context) : RecyclerView.Adapter<HomeItemAdapter.ViewHolder>(){

    var items = ArrayList<ItemData>()
    private val baseURL = BASE_URL

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_item_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeItemAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun filterList(filteredList: ArrayList<ItemData>){
        items = filteredList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemName: TextView = itemView.findViewById(R.id.tv_itemName)
        private val itemDateStart: TextView = itemView.findViewById(R.id.tv_itemDateStartShow)
        private val itemDateEnd: TextView = itemView.findViewById(R.id.tv_itemDateEndShow)
        private val itemPrice: TextView = itemView.findViewById(R.id.tv_itemPriceShow)
        private val itemImage: ImageView = itemView.findViewById(R.id.iv_itemImage_recycler)

        fun bind(item : ItemData) {
            itemName.text = item.name
            itemDateStart.text = item.date_start+" ~ "
            itemDateEnd.text = item.date_end
            itemPrice.text = item.price.toString()+"원"

//            val imageBytes = Base64.decode(item.image, Base64.DEFAULT)
//            Log.d("---image---", "$imageBytes")
//            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//            itemImage.setImageBitmap(decodedImage)  // NULL image 처리 안해도 되나?

            itemView.setOnClickListener{
                //item_id를 item detail page에 넘기기
                val intent = Intent(context, ItemDetailActivity::class.java)
                intent.putExtra("item_id", item.item_id)
                context.startActivity(intent)
            }

        }
    }


}