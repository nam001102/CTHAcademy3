package com.cthacademy.android

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class LoadingScreenActivity : AppCompatActivity() {
    private lateinit var outter : ImageView
    private lateinit var inner : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_loading_screen)

        outter = findViewById(R.id.outter)
        inner = findViewById(R.id.inner)

        val fadeInAnimation= AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeRotateAnimation= AnimationUtils.loadAnimation(this, R.anim.fade_rotate)


        runBlocking {
            delayFunction(4000) // Trì hoãn 2 giây
        }

        val countDownTimer = object : CountDownTimer(3000, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                // Mỗi lần đếm ngược (tick), hàm này được gọi
            }

            override fun onFinish() {
                // Khi đếm ngược kết thúc, hàm này được gọi
                makeViewVisible(outter)
                outter.startAnimation(fadeRotateAnimation)
            }
        }
        countDownTimer.start()
        inner.startAnimation(fadeInAnimation)
        Handler().postDelayed({
            val intent = Intent(this@LoadingScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 6000)
    }
}
fun makeViewVisible(view: View) {
    view.visibility = View.VISIBLE
}
suspend fun delayFunction(delayMillis: Long) {
    delay(delayMillis)
}