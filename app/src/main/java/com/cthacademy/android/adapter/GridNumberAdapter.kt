package com.cthacademy.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.cthacademy.android.R

class GridNumberAdapter(private val numbers: List<Int>) : BaseAdapter() {

    override fun getCount(): Int {
        return numbers.size
    }

    override fun getItem(position: Int): Any {
        return numbers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.grid_array_number_item, parent, false)
        val imageView: ImageView = view.findViewById(R.id.grid_number_item)
        // Load the image resource from the integer value

        imageView.setImageResource(intToImageResource(numbers[position]))


        return imageView
    }
    private fun intToImageResource(number: Int): Int {
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
            14 -> R.drawable.number_14
            21 -> R.drawable.number_21
            25 -> R.drawable.number_25
            28 -> R.drawable.number_28
            29 -> R.drawable.number_29
            30 -> R.drawable.number_30
            31 -> R.drawable.number_31
            32 -> R.drawable.number_32
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