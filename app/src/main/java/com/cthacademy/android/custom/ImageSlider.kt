package com.cthacademy.android.custom

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.GestureDetectorCompat
import java.util.*
import kotlin.math.abs

class ImageSlider(
    private val context: Context,
    private val imageView: ImageView,
    private var images: List<Int> = emptyList(),
) {
    private var currentPosition: Int = 0
    private var timer: Timer? = null

    private var clickListeners: OnItemClickListener? = null
    private var positionChangedListener: OnPositionChangedListener? = null
    private lateinit var gestureDetector: GestureDetectorCompat

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    interface OnPositionChangedListener {
        fun onPositionChanged(position: Int)
    }

    fun getItemCount(): Int {
        return images.size
    }

    fun setImages(images: List<Int>) {
        this.images = images
    }

    fun setOnPositionChangedListener(listener: OnPositionChangedListener) {
        positionChangedListener = listener
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        clickListeners = listener
    }

    fun startSlideshow() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                currentPosition = (currentPosition + 1) % images.size
                updateImageWithSlideInAnimation()
            }
        }, 0, 8000)

        setupGestureDetector()
    }

    fun stopSlideshow() {
        timer?.cancel()
        timer = null
        gestureDetector.setOnDoubleTapListener(null)
    }

    private fun updateImage() {
        val currentImage = images[currentPosition]
        val handler = Handler(imageView.context.mainLooper)
        handler.post {
            imageView.setImageResource(currentImage)
            positionChangedListener?.onPositionChanged(currentPosition)
        }
    }
    private fun updateImageWithSlideInAnimation() {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val slideOutAnimatorX = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 0f, -imageView.width.toFloat())
            slideOutAnimatorX.duration = 250
            slideOutAnimatorX.interpolator = AccelerateInterpolator()

            slideOutAnimatorX.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}

                override fun onAnimationEnd(p0: Animator) {
                    updateImage()
                    val slideInAnimatorX = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, imageView.width.toFloat(), 0f)
                    slideInAnimatorX.duration = 250
                    slideInAnimatorX.interpolator = DecelerateInterpolator()
                    slideInAnimatorX.start()
                }

                override fun onAnimationCancel(p0: Animator) {}

                override fun onAnimationRepeat(p0: Animator) {}
            })
            slideOutAnimatorX.start()

        }
    }
    private fun swipeToUpdateImageWithSlideInAnimation(leftOrRight: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            if (leftOrRight == "left") {
                val slideOutAnimatorX = ObjectAnimator.ofFloat(
                    imageView,
                    View.TRANSLATION_X,
                    0f,
                    -imageView.width.toFloat()
                )
                slideOutAnimatorX.duration = 250
                slideOutAnimatorX.interpolator = AccelerateInterpolator()

                slideOutAnimatorX.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {}

                    override fun onAnimationEnd(p0: Animator) {
                        updateImage()
                        val slideInAnimatorX = ObjectAnimator.ofFloat(
                            imageView,
                            View.TRANSLATION_X,
                            imageView.width.toFloat(),
                            0f
                        )
                        slideInAnimatorX.duration = 250
                        slideInAnimatorX.interpolator = DecelerateInterpolator()
                        slideInAnimatorX.start()
                    }

                    override fun onAnimationCancel(p0: Animator) {}

                    override fun onAnimationRepeat(p0: Animator) {}
                })
                slideOutAnimatorX.start()
            } else if (leftOrRight == "right") {

                val slideOutAnimatorX = ObjectAnimator.ofFloat(
                    imageView,
                    View.TRANSLATION_X,
                    0f,
                    imageView.width.toFloat()
                )
                slideOutAnimatorX.duration = 250
                slideOutAnimatorX.interpolator = AccelerateInterpolator()

                slideOutAnimatorX.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        updateImage()
                        val slideInAnimatorX = ObjectAnimator.ofFloat(
                            imageView,
                            View.TRANSLATION_X,
                            -imageView.width.toFloat(),
                            0f
                        )
                        slideInAnimatorX.duration = 250
                        slideInAnimatorX.interpolator = DecelerateInterpolator()
                        slideInAnimatorX.start()
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })

                slideOutAnimatorX.start()
            }
        }

    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetectorCompat(imageView.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                val position = getCurrentPosition()
                clickListeners?.onItemClick(position)
                return true
            }
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val deltaX = e2.x.minus(e1?.x ?: 0f) ?: 0f
                if (abs(deltaX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (deltaX < 0) {
                        // Swiped from right to left
                        currentPosition = (currentPosition + 1) % images.size
                        swipeToUpdateImageWithSlideInAnimation("left")
                    } else {
                        // Swiped from left to right
                        currentPosition = if (currentPosition > 0) currentPosition - 1 else images.size - 1
                        swipeToUpdateImageWithSlideInAnimation("right")
                    }
                    return true
                }
                return false
            }
        })

        imageView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    fun getCurrentPosition(): Int {
        return currentPosition
    }

    companion object {
        private const val SWIPE_THRESHOLD_VELOCITY = 200
    }
}