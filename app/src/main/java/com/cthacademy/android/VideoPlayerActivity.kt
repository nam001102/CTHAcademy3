package com.cthacademy.android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class VideoPlayerActivity : AppCompatActivity() {

    private var videoTitle: String? = null
    private var videoUrl: String? = null
    private var seekbar: Long? = null

//    private lateinit var title: TextView
    private lateinit var playerViewLandscape: PlayerView
//    private lateinit var progressBar_landscape: ProgressBar
    private lateinit var fullScreenBnt: ImageView

    private lateinit var playerLandscape: SimpleExoPlayer

    private var isFullscreen = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_video_player)

        // Get the video details from the intent extras
        videoUrl = intent.getStringExtra("videoUrl")
        seekbar = intent.getLongExtra("seekbar",0)
        isFullscreen = intent.getBooleanExtra("isFullScreen",true)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (isFullscreen){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }

        // Set the video title and description\
        fullScreenBnt = findViewById(R.id.exo_fullscreen)
//        title.text = videoTitle

        // Bind the player to the player view
        playerViewLandscape = findViewById(R.id.player_view_landscape)

        playerLandscape = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        playerViewLandscape.player = playerLandscape


        // Set up the orientation change listener

        fun onConfigurationChanged(newConfig: Configuration) {
            super.onConfigurationChanged(newConfig)

            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerViewLandscape.visibility = View.VISIBLE
                playerLandscape.playWhenReady = true

            }
        }

        fullScreenBnt.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("seekbarPosition", playerLandscape.currentPosition)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        // Register the orientation change listener
        onConfigurationChanged(Configuration())
        initializePlayer()
    }


    private fun initializePlayer() {


        // Create a new ExoPlayer instance
        val player = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()


        // Build the video URI
        val uri = Uri.parse(videoUrl)

        // Create a Firebase storage reference
        val storageRef = Firebase.storage.reference
        // Get the video file from Firebase storage
        val videoRef = storageRef.child(uri.toString())
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Download the video file to a local cache
                val localFile = File.createTempFile("video", "mp4")
                videoRef.getFile(localFile).await()
                // Load the video from the local cache
                val mediaItem = MediaItem.fromUri(Uri.fromFile(localFile))
                withContext(Dispatchers.Main) {

                    playerLandscape.setMediaItem(mediaItem)
                    playerLandscape.prepare()
                    playerLandscape.seekTo(seekbar ?: 0)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            playerLandscape.playWhenReady = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            playerLandscape.playWhenReady = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerLandscape.release()
    }

}