package com.cthacademy.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.R

class MissingNumberAdapter(private val documentList: List<Int>, private val isAdmin:Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_TYPE_DATA = 0
    private val ITEM_TYPE_BUTTON = 1


    private var buttonClickListener: ButtonClickListener? = null

    interface ButtonClickListener {
        fun onButtonClick(position: Int)
        fun onAdminButtonClick()
    }

    override fun getItemCount(): Int {
        return if (isAdmin){
            documentList.size + 1
        }else{
            documentList.size
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (isAdmin){
            if (position == 0) {
                ITEM_TYPE_BUTTON
            } else {
                ITEM_TYPE_DATA
            }
        }else{
            ITEM_TYPE_DATA
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.ic_container, parent, false)
                DocumentViewHolder(view)
            }
            ITEM_TYPE_BUTTON -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.ic_admin, parent, false)
                ButtonViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && holder is ButtonViewHolder) {
            if (isAdmin) {
                holder.adminView.visibility = View.VISIBLE
                holder.adminView.setOnClickListener {
                    buttonClickListener?.onAdminButtonClick()
                }
            } else {
                holder.adminView.visibility = View.GONE
            }
        } else if (holder is DocumentViewHolder) {
            if (isAdmin){
                val data = documentList[position-1]
                holder.imageView.setImageResource(intToImageResource(data))
                holder.imageView.setBackgroundResource(R.drawable.ic_box)
                holder.textView.visibility = View.GONE

                holder.itemView.setOnClickListener {
                    buttonClickListener?.onButtonClick(position-1)
                }
            }else{
                val data = documentList[position]
                holder.imageView.setImageResource(intToImageResource(data))
                holder.imageView.setBackgroundResource(R.drawable.ic_box)
                holder.textView.visibility = View.GONE

                holder.itemView.setOnClickListener {
                    buttonClickListener?.onButtonClick(position)
                }
            }

        }
    }

    fun setButtonClickListener(listener: ButtonClickListener) {
        buttonClickListener = listener
    }

    inner class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.Stats_number)
        val textView: TextView = view.findViewById(R.id.Stats_title)
    }

    inner class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val adminView: ImageView = view.findViewById(R.id.Admin_btn)
    }
    private fun intToImageResource(number: Int?): Int {
        return when (number) {
            1 -> R.drawable.number1
            2 -> R.drawable.number2
            3 -> R.drawable.number3
            4 -> R.drawable.number4
            5 -> R.drawable.number5
            6 -> R.drawable.number6
            7 -> R.drawable.number7
            8 -> R.drawable.number8
            9 -> R.drawable.number9
            11 -> R.drawable.number11
            13 -> R.drawable.number_13
            14 -> R.drawable.number_14
            22 -> R.drawable.number22
            21 -> R.drawable.number_21
            25 -> R.drawable.number_25
            28 -> R.drawable.number_28
            29 -> R.drawable.number_29
            30 -> R.drawable.number_30
            31 -> R.drawable.number_31
            32 -> R.drawable.number_32
            33 -> R.drawable.number33
            34 -> R.drawable.number_34
            35 -> R.drawable.number_35
            36 -> R.drawable.number_36
            37 -> R.drawable.number_37
            38 -> R.drawable.number_38
            39 -> R.drawable.number_39
            40 -> R.drawable.number_40
            41 -> R.drawable.number_41
            42 -> R.drawable.number_42
            43 -> R.drawable.number_43
            44 -> R.drawable.number_44
            45 -> R.drawable.number_45
            46 -> R.drawable.number_46
            47 -> R.drawable.number_47
            48 -> R.drawable.number_48
            49 -> R.drawable.number_49
            50 -> R.drawable.number_50
            51 -> R.drawable.number_51
            52 -> R.drawable.number_52
            53 -> R.drawable.number_53
            54 -> R.drawable.number_54
            55 -> R.drawable.number_55
            56 -> R.drawable.number_56
            57 -> R.drawable.number_57
            58 -> R.drawable.number_58
            59 -> R.drawable.number_59
            60 -> R.drawable.number_60
            61 -> R.drawable.number_61
            62 -> R.drawable.number_62
            0 -> R.drawable.number_0
            else -> R.drawable.number0
        }
    }
}
