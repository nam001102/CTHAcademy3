package com.cthacademy.android.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class BookLayout(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val width = (height * 0.7).toInt()
        val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        super.onMeasure(newWidthMeasureSpec, heightMeasureSpec)
    }
}
