package com.cthacademy.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.R

class StatsItemNumberAdapter(private val stats: List<Int>) :
    RecyclerView.Adapter<StatsItemNumberAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onButtonClick(position: Int)
    }

    private var onClickListener: OnClickListener? = null

    fun setButtonClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ic_stats_number, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = stats[position]
        holder.statsitemNumber.text = data.toString()

        holder.itemView.setOnClickListener {
            onClickListener?.onButtonClick(position)
        }
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statsitemNumber: TextView = view.findViewById(R.id.Stats_item_text)
    }
}
