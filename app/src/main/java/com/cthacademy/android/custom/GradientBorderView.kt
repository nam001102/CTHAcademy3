package com.cthacademy.android.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.cthacademy.android.R

class GradientBorderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private var startColor: Int = Color.RED,     // Default start color is red
    private var endColor: Int = Color.BLUE       // Default end color is blue
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val transparentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    init {
        // Read custom attributes
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.GradientBorderView, defStyleAttr, 0
        )
        startColor = typedArray.getColor(
            R.styleable.GradientBorderView_startColor, ContextCompat.getColor(context, R.color.gradient_border_start)
        )
        endColor = typedArray.getColor(
            R.styleable.GradientBorderView_endColor, ContextCompat.getColor(context, R.color.gradient_border_end)
        )
        typedArray.recycle()

        // Set up the gradient border paint
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 10f

        // Set up the transparent inside paint
        transparentPaint.color = Color.TRANSPARENT
        transparentPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()

        // Create a LinearGradient for the gradient border
        val shader = LinearGradient(
            0f, 0f, width, height,
            startColor, endColor, Shader.TileMode.CLAMP
        )

        // Draw the gradient border on the canvas
        borderPaint.shader = shader
        canvas.drawRoundRect(RectF(0f, 0f, width, height), width / 2, height / 2, borderPaint)

        // Define the inner circle (you can adjust the position and size as needed)
        val circleX = width / 2
        val circleY = height / 2
        val circleRadius = width / 4

        // Create a Path for the inner circle
        path.reset()
        path.addCircle(circleX, circleY, circleRadius, Path.Direction.CW)

        // Set the inner circle as the clip path
        canvas.clipPath(path, Region.Op.DIFFERENCE)

        // Draw the transparent inside
        canvas.drawRect(0f, 0f, width, height, transparentPaint)

        // Draw the child views inside the layout
        super.onDraw(canvas)
    }
}
