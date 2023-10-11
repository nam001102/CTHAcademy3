package com.cthacademy.android

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

class BitmapBlur {

    companion object {
        fun blurBitmap(context: Context, sourceBitmap : Bitmap, radius: Int = 25): Bitmap {
            val renderScript = RenderScript.create(context)
            val input = Allocation.createFromBitmap(renderScript,sourceBitmap)
            val output = Allocation.createTyped(renderScript, input.type)

            val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
            scriptIntrinsicBlur.setInput(input)

            val r = when{
                radius <= 0 -> {
                    1
                }
                radius > 25 -> {
                    25
                }
                else -> {
                    radius
                }
            }

            scriptIntrinsicBlur.setRadius(r.toFloat())
            scriptIntrinsicBlur.forEach(output)
            output.copyTo(sourceBitmap)

            renderScript.destroy()

            return sourceBitmap
        }
    }
}