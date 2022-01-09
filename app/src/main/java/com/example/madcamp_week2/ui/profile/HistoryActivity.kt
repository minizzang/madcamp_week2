package com.example.madcamp_week2.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.ui.items.ItemDataInList

class HistoryActivity : AppCompatActivity() {

    lateinit var lendListView : RecyclerView
    lateinit var borrowListView : RecyclerView

    private val lendItemArray = ArrayList<ItemDataInList>()
    private val borrowItemArray = ArrayList<ItemDataInList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        for(i: Int in 0..10) {
            lendItemArray.add(ItemDataInList("충전기", "minsuh", "21-01-01 ~ 21-01-03"))
            borrowItemArray.add(ItemDataInList("명품백", "hello", "21-12-23 ~ 21-12-31"))
        }

        lendListView = findViewById<RecyclerView>(R.id.lendList)
        borrowListView = findViewById<RecyclerView>(R.id.borrowList)

        var adapterLend = LendItemAdapter(this, lendItemArray)
        var adapterBorrow = BorrowItemAdapter(this, borrowItemArray)

        lendListView.adapter = adapterLend
        borrowListView.adapter = adapterBorrow

    }
}