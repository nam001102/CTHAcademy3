package com.cthacademy.android.adapter

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cthacademy.android.R
import java.util.*

class CalenderAdapter (private val context: Context) : BaseAdapter() {

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

    override fun getCount(): Int {
        // Return the total number of days in the current month
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    override fun getItem(position: Int): Any {
        // Not used in this example
        return ""
    }

    override fun getItemId(position: Int): Long {
        // Not used in this example
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            // Inflate the calendar day item layout if convertView is null
            view = LayoutInflater.from(context).inflate(R.layout.grid_calender, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Calculate the day offset for the first day of the month
        val firstDayOffset = calendar.get(Calendar.DAY_OF_WEEK) - 1

        // Calculate the actual day value for the current position
        val dayValue = position + 1 - firstDayOffset

        // Set the day text
        viewHolder.dayTextView.text = if (dayValue > 0) dateFormat.format(getDate(dayValue)) else ""

        return view
    }
    private fun getDate(day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.time
    }

    private class ViewHolder(view: View) {
        val dayTextView: TextView = view.findViewById(R.id.day)
    }
}