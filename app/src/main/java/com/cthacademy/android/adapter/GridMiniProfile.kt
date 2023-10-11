package com.cthacademy.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.R
import com.cthacademy.android.modal.MiniProfile

class GridMiniProfile(private val documentList: List<MiniProfile>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_TYPE_DATA = 0
    private val ITEM_TYPE_BUTTON = 1

    private var buttonClickListener: ButtonClickListener? = null

    interface ButtonClickListener {
        fun onButtonClick()
    }

    override fun getItemCount(): Int {
        return documentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == documentList.size) {
            ITEM_TYPE_BUTTON
        } else {
            ITEM_TYPE_DATA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_mini_profile_item, parent, false)
                DocumentViewHolder(view)
            }
            ITEM_TYPE_BUTTON -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_mini_profile_buton, parent, false)
                ButtonViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DocumentViewHolder) {
            val data = documentList[position]
            holder.nameTextView.text = data.name
            holder.dateTextView.text = data.date
        } else if (holder is ButtonViewHolder) {

            holder.buttonView.setOnClickListener {
                buttonClickListener?.onButtonClick()
            }
        }
    }

    fun setButtonClickListener(listener: ButtonClickListener) {
        buttonClickListener = listener
    }

    inner class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.avatar)
        val nameTextView: TextView = view.findViewById(R.id.name)
        val dateTextView: TextView = view.findViewById(R.id.date)
    }

    inner class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonView: ImageView = view.findViewById(R.id.btn)
    }
}
