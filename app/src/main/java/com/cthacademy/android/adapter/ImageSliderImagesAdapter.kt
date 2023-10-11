package com.cthacademy.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.R

class ImageSliderImagesAdapter (
    private val images: List<Int>
) : RecyclerView.Adapter<ImageSliderImagesAdapter.ViewHolder>() {

    private var currentPosition = 0

    fun setCurrentPosition(position: Int) {
        currentPosition = position
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ic_item_imageslider, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageResId = images[position]
        holder.imageView.setImageResource(R.color.transparent)

        if (position == currentPosition) {
            // Change the color of the ImageView here
            holder.imageView.setImageResource(R.color.ImageSliderOnSelected)
        } else {
            // Reset the color (if needed)
            holder.imageView.setImageResource(R.color.transparent)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ImageSlider_image) // Replace with your ImageView ID
    }
}