package com.cthacademy.android.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.cthacademy.android.R

class VideoContainerLayout@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width / 3 // Set height as one-third of the width
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    private var cornerRadius = 0f // Adjust the corner radius as needed

    private val path = Path()

    init {
        // Retrieve the cornerRadius attribute value from XML
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VideoContainerLayout)
        cornerRadius = typedArray.getDimension(
            R.styleable.VideoContainerLayout_cornerRadius,
            cornerRadius
        )
        typedArray.recycle()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val count = canvas.save()

        path.reset()
        val bounds = canvas.clipBounds
        val radius = cornerRadius

        path.addRoundRect(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat(),
            radius,
            radius,
            Path.Direction.CW
        )

        canvas.clipPath(path)
        super.dispatchDraw(canvas)

        canvas.restoreToCount(count)
    }
}