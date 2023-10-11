package com.cthacademy.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UploadVideoActivity : AppCompatActivity() {
    private lateinit var storageRef: StorageReference

    private val videoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { videoUri: Uri? ->
            videoUri?.let { uri ->
                uploadVideoToFirebaseStorage(uri)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.layout_checkout)

        storageRef = FirebaseStorage.getInstance().reference

        // Launch the video picker when needed
        launchVideoPicker()
    }

    private fun launchVideoPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        videoPickerLauncher.launch(intent.toString())
    }

    private fun uploadVideoToFirebaseStorage(videoUri: Uri) {
        val videoFileName = "your_video_filename.mp4" // Set your desired filename
        val videoRef: StorageReference = storageRef.child(videoFileName)

        val metadata = StorageMetadata.Builder()
            .setContentType("video/mp4")
            .build()

        val uploadTask: UploadTask = videoRef.putFile(videoUri, metadata)

        // Rest of the upload and completion handling code
        // Same as mentioned in the previous response
    }
}