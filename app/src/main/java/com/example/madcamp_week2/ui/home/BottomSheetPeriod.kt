package com.example.madcamp_week2.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import com.example.madcamp_week2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class BottomSheetPeriod(val itemClick: (Int) -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.search_filter_period, container, false)

        val startDate = root.findViewById<Button>(R.id.filterDateStart)
        val endDate = root.findViewById<Button>(R.id.filterDateEnd)
        val confirmBtn = root.findViewById<Button>(R.id.confirm)

        startDate.setOnClickListener{
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    startDate.text = "${year}-${month+1}-${dayOfMonth}"
                }
            }, year, month, date)

            dlg.show()
        }

        endDate.setOnClickListener{
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    endDate.text = "${year}-${month+1}-${dayOfMonth}"
                }
            }, year, month, date)

            dlg.show()
        }

        //confirmBtn.setOnClickListener {

        //    dismiss()
        //}

        return root
    }
}