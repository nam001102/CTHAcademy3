package com.cthacademy.android.adapter

import com.cthacademy.android.modal.VideoModal
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cthacademy.android.R
import com.cthacademy.android.VideoPlayerActivity

class GridVideoAdapter (private val context: Context, private val videos: List<VideoModal>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return videos.size
    }

    override fun getItem(position: Int): Any {
        return videos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val video = videos[position]

        // Inflate the video_item layout
        val view = LayoutInflater.from(context).inflate(R.layout.grid_lesson_videos, null)

        // Get references to the views in the layout
        val thumbnailView = view.findViewById<ImageView>(R.id.Video_thumbnail)
        val titleView = view.findViewById<TextView>(R.id.Video_title)
        val descriptionView = view.findViewById<TextView>(R.id.Video_description)

        // Set the title and description views
        titleView.text = video.title
        descriptionView.text = video.description

        // Set the click listener for the video thumbnail
        thumbnailView.setOnClickListener {
            // Create an Intent to start the com.app.gainacademy.android.VideoPlayerActivity
            val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                // Pass the video ID, thumbnail URL, video URL, title, and description to the com.app.gainacademy.android.VideoPlayerActivity
                putExtra("thumbnailUrl", video.thumbnailUrl)
                putExtra("videoUrl", video.videoUrl)
                putExtra("title", video.title)
                putExtra("description", video.description)
            }
            // Start the com.app.gainacademy.android.VideoPlayerActivity
            context.startActivity(intent)
        }
        return view
    }

    private class ViewHolder(view: View) {
        val thumbnailImageView: ImageView = view.findViewById(R.id.Video_thumbnail)
        val titleTextView: TextView = view.findViewById(R.id.Video_title)
        val descriptionTextView: TextView = view.findViewById(R.id.Video_description)
    }
}