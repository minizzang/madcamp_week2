package com.example.madcamp_week2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.madcamp_week2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetPrice : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_filter_price, container, false)
    }
}