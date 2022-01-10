package com.example.madcamp_week2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.madcamp_week2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetPrice(val itemClick: (Int) -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_filter_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.findViewById<Button>(R.id.free)?.setOnClickListener {
            itemClick(0)
            dismiss()
        }
        view?.findViewById<Button>(R.id.below500)?.setOnClickListener {
            itemClick(1)
            dismiss()
        }
        view?.findViewById<Button>(R.id.below1000)?.setOnClickListener {
            itemClick(2)
            dismiss()
        }
        view?.findViewById<Button>(R.id.below2000)?.setOnClickListener {
            itemClick(3)
            dismiss()
        }
        view?.findViewById<Button>(R.id.below3000)?.setOnClickListener {
            itemClick(4)
            dismiss()
        }
        view?.findViewById<Button>(R.id.above3000)?.setOnClickListener {
            itemClick(5)
            dismiss()
        }
        view?.findViewById<Button>(R.id.showAll)?.setOnClickListener {
            itemClick(6)
            dismiss()
        }
    }
}