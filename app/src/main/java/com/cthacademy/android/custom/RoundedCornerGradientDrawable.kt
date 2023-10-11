package com.cthacademy.android.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.cthacademy.android.R

class RoundedCornerGradientDrawable(private val context: Context,private val start : Int,private val end : Int) : Drawable() {

    private val gradientColors = intArrayOf(
        darkenColor(ContextCompat.getColor(context, start), 1f),
        darkenColor(ContextCompat.getColor(context, end), 1f)
//        ContextCompat.getColor(context, end)
    )

    private val cornerRadius = context.resources.getDimension(R.dimen.corner_radius)
    private val blurRadius = 50.0f

    override fun draw(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()

        // Calculate the dimensions for the smaller rounded rectangle
        val smallerWidth = width * 0.9f
        val smallerHeight = height * 0.9f
        val smallerX = (width - smallerWidth) / 2
        val smallerY = (height - smallerHeight) / 2

        // Create the paint for the gradient
        val gradientPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                gradientColors, null, Shader.TileMode.CLAMP
            )
            maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
            isAntiAlias = true
        }

        // Create the path for the rounded rectangle
        val path = Path().apply {
            addRoundRect(
                RectF(smallerX, smallerY, smallerX + smallerWidth, smallerY + smallerHeight),
                cornerRadius, cornerRadius, Path.Direction.CW
            )
        }

        // Draw the rounded rectangle with the gradient background
        canvas.drawPath(path, gradientPaint)

    }

    private fun darkenColor(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = (Color.red(color) * factor).toInt()
        val g = (Color.green(color) * factor).toInt()
        val b = (Color.blue(color) * factor).toInt()
        return Color.argb(a, r, g, b)
    }

    override fun setAlpha(alpha: Int) {
        // No-op
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // No-op
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }
}
