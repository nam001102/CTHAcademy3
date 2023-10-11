package com.cthacademy.android.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TransparentTextTextView : AppCompatTextView {
    private var mMaskBitmap: Bitmap? = null
    private var mMaskCanvas: Canvas? = null
    private var mPaint: Paint? = null
    private var mBackground: Drawable? = null
    private var mBackgroundBitmap: Bitmap? = null
    private var mBackgroundCanvas: Canvas? = null
    private var mSetBoundsOnSizeAvailable = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        super.setTextColor(Color.BLACK)
        super.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    @Deprecated("Deprecated in Java")
    override fun setBackgroundDrawable(background: Drawable?) {
        mBackground = background
        val w = background?.intrinsicWidth
        val h = background?.intrinsicHeight
        // Drawable has no dimensions, retrieve View's dimensions
        if (w == -1 || h == -1) {
            mSetBoundsOnSizeAvailable = true
            return
        }

        if (w != null) {
            if (h != null) {
                background.setBounds(0, 0, w, h)
            }
        }
        invalidate()
    }

    override fun setBackgroundColor(color: Int) {
        setBackgroundDrawable(ColorDrawable(color))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBackgroundBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mBackgroundCanvas = Canvas(mBackgroundBitmap!!)
        mMaskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mMaskCanvas = Canvas(mMaskBitmap!!)

        if (mSetBoundsOnSizeAvailable) {
            mBackground!!.setBounds(0, 0, w, h)
            mSetBoundsOnSizeAvailable = false
        }
    }

    override fun onDraw(canvas: Canvas) {
        // Draw background
        mBackground?.draw(mBackgroundCanvas!!)
        // Draw mask
        mMaskCanvas!!.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)
        super.onDraw(mMaskCanvas!!)

        mBackgroundCanvas!!.drawBitmap(mMaskBitmap!!, 0f, 0f, mPaint!!)
        canvas.drawBitmap(mBackgroundBitmap!!, 0f, 0f, null)
    }
}