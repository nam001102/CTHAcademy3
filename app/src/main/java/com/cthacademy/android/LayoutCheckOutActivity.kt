package com.cthacademy.android

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class LayoutCheckOutActivity : AppCompatActivity() {

    private lateinit var frame: FrameLayout

    private lateinit var name: TextView
    private lateinit var point: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.layout_checkout)
//        name = findViewById(R.id.checkout_name)
//        point = findViewById(R.id.checkout_point)
//        frame = findViewById(R.id.checkout_frame)

        val phone = intent.getSerializableExtra("Phone")

        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("users").document(phone.toString())
        userDocRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // if the document exists, get the user's data as a custom data class
                val dbname = document.getString("name")
                val dbdate = document.getString("date")
                val dbpoint = document.getLong("point")
                name.text = dbname
                point.text = dbpoint.toString() + " $"
            }
        }
        val fade_out_anim = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.fade_out
        )

//        val mSlideView: SlideView = findViewById<View>(R.id.slide_view) as SlideView
//        mSlideView.setOnChangeListener(object : OnChangeListener {
//            override fun onProgressChanged(progress: Int) {
//                val opacity = 1 - (progress / 100f)
//                lock_screen_text.alpha = opacity
//            }
//
//            override fun onStopChanged() {
//
//            }
//
//            override fun onComplete() {
//                Handler().postDelayed(Runnable {
//                    val intent = Intent(this@LayoutCheckOutActivity, ShopActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }, 0)
//            }
//        })

//        val switch = findViewById<SwitchCompat>(R.id.swOnOff).isChecked
//        if (switch){
//
//        }

    }

}