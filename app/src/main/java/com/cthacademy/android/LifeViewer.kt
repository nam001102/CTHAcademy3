package com.cthacademy.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.util.Base64
import android.widget.Toast

class LifeViewer : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var title : TextView
    private lateinit var meaning : TextView
    private lateinit var number : ImageView
    private lateinit var txt1 : TextView
    private lateinit var txt2 : TextView
    private lateinit var txt3 : TextView
    private lateinit var txt4 : TextView
    private lateinit var speaker1 : ImageView
    private lateinit var speaker2 : ImageView
    private lateinit var speaker3 : ImageView
    private lateinit var speaker4 : ImageView
    private lateinit var image : ImageView

    private lateinit var tts: TextToSpeech
    private var paused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.layout_life)

//        tts = TextToSpeech(this, this)
//
//        title=findViewById(R.id.title)
//
//        txt1 = findViewById(R.id.txt)
//        txt2 = findViewById(R.id.txt2)
//        txt3 = findViewById(R.id.txt3)
//        txt4 = findViewById(R.id.txt4)
//        meaning = findViewById(R.id.meaning)
//
//        speaker1 = findViewById(R.id.Speaker)
//        speaker2 = findViewById(R.id.Speaker2)
//        speaker3 = findViewById(R.id.Speaker3)
//        speaker4 = findViewById(R.id.Speaker4)

//        image = findViewById(R.id.Image)
//
//        number = findViewById(R.id.ic_number_img)

        val Number = intent.getStringExtra("Number")
        val Title = intent.getStringExtra("Title")
        val Challange = intent.getStringExtra("Challange")
        val Energy = intent.getStringExtra("Energy")
        val Environment = intent.getStringExtra("Environment")
        val Lesson = intent.getStringExtra("Lesson")
        val Meaning = intent.getStringExtra("Meaning")
        val Image = intent.getStringExtra("Image")
        val tts = intent.getStringExtra("Tts")

        number.setImageResource(StringToImageResource(Number))
        val bitmap = Image?.let { decodeBase64ToBitmap(it) }
        if (bitmap != null) {
            image.setImageBitmap(bitmap)
        }
        title.text = Title.toString()
        meaning.text = Spacing(Meaning.toString())
        txt1.text = Spacing(Energy.toString())
        txt2.text = Spacing(Challange.toString())
        txt3.text = Spacing(Lesson.toString())
        txt4.text = Spacing(Environment.toString())
        val challange = Spacing(Challange.toString())
        val energy = Spacing(Energy.toString())
        val lesson = Spacing(Lesson.toString())
        val environment = Spacing(Environment.toString())
        if (tts != null) {
            speak(tts)
        }else{
            Toast.makeText(this, "Chức năng này chưa được thêm.",Toast.LENGTH_SHORT).show()
        }


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
    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

        // Pause TextToSpeech on button click
        speaker1.setOnClickListener {
            if (!paused) {
                speaker1.setImageResource(R.drawable.ic_speaker_mute)
                tts.stop()
                paused = true
            } else {
                speaker1.setImageResource(R.drawable.ic_speaker)
                paused = false
                speak(text)
            }
        }
    }
}

fun Spacing(str: String): String {
    return str.replace("\\n", "\n").replace("\n","\n").replace("\\t","\t").replace("\t","\t")
}

fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun StringToImageResource(str: String?): Int {
    return when (str) {
        "1" -> R.drawable.number1
        "2" -> R.drawable.number2
        "3" -> R.drawable.number3
        "4" -> R.drawable.number4
        "5" -> R.drawable.number5
        "6" -> R.drawable.number6
        "7" -> R.drawable.number7
        "8" -> R.drawable.number8
        "9" -> R.drawable.number9
        "11" -> R.drawable.number11
        "22" -> R.drawable.number22
        "33" -> R.drawable.number33
        else -> R.drawable.number0
    }
}
