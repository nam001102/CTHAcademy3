package com.cthacademy.android

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LayoutItemActitivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.layout_life)

        // Initialize TextToSpeech
        tts = TextToSpeech(this, this)
//        val isLesson = intent.getBooleanExtra("isLesson")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set language for TextToSpeech
            val result = tts.setLanguage(Locale("vi", "VN"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported")
            } else {
                // Set the female voice
                tts.voice = Voice("vi-vn-x-oda#female_1-local", Locale("vi", "VN"), Voice.QUALITY_HIGH, Voice.LATENCY_HIGH, false, null)
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    // Release TextToSpeech resources when the activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}