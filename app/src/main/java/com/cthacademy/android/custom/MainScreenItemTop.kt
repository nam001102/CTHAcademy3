package com.cthacademy.android.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class MainScreenItemTop(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width / 1.8 // Set height as three times the width
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }
}