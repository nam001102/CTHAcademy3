package com.cthacademy.android.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class RectangleRelativeLayout(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width * 2.03 // Set height as three times the width
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }
}