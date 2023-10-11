package com.cthacademy.android.adapter

import com.cthacademy.android.modal.MiniProfile
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.R

class MiniProfileAdapter(private val documentList: MutableList<MiniProfile>) :
    RecyclerView.Adapter<MiniProfileAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onMiniProfileClick(position: Int)
    }

    private var onClickListener: OnClickListener? = null

    fun setMiniProfileClickListener(listener: OnClickListener) {
        onClickListener = listener
    }
    fun insertData(newData: MiniProfile) {
        documentList.add(newData)
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ic_miniprofile, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = documentList[position]
        holder.itemName.text = data.name?.split(" ")?.last()
        holder.itemDate.text = data.date
        holder.itemView.setOnClickListener {
            onClickListener?.onMiniProfileClick(position)
        }
        holder.itemName.setOnClickListener {
            onClickListener?.onMiniProfileClick(position)
        }
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.Miniprofile_name)
        val itemDate: TextView = view.findViewById(R.id.Miniprofile_date)
    }

}
