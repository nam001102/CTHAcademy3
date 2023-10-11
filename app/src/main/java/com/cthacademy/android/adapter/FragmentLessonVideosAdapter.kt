package com.cthacademy.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.R
import com.cthacademy.android.modal.LessonVideoModal
import com.google.firebase.storage.FirebaseStorage
import jp.wasabeef.blurry.Blurry
import java.io.ByteArrayInputStream

class FragmentLessonVideosAdapter (private val documentList: List<LessonVideoModal>, private val context: Context) : RecyclerView.Adapter<FragmentLessonVideosAdapter.DocumentVideoHolder>() {


    private var itemClickListener: OnItemClickListener? = null
    private var filteredList: List<LessonVideoModal> = documentList

    @SuppressLint("NotifyDataSetChanged")
    fun filterByTitle(query: String) {
        filteredList = documentList.filter { document ->
            document.title.contains(query, ignoreCase = true)
        }
        notifyDataSetChanged()
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener

    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onUnlockVideoClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentVideoHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_lesson_videos, parent, false)
        return DocumentVideoHolder(itemView)
    }

    override fun onBindViewHolder(holder: DocumentVideoHolder, position: Int) {
        val documentItem = filteredList[position]

        // Set the image, title, and description to the views in the item view


        holder.titleTextView.text = documentItem.title
        holder.descriptionTextView.text = documentItem.description


        val videoUri = documentItem.uri

        val storageRef = FirebaseStorage.getInstance().reference
        val fileReference = storageRef.child(videoUri)

        fileReference.downloadUrl.addOnSuccessListener { uri ->
            // Use the download URL (URI) here
            val downloadUri = uri.toString()

            if (documentItem.isLocked){
                holder.videoImageView.setImageBitmap(convertBase64ToImage(context,documentItem.image))
                Handler().postDelayed({
                    val bitmap = Blurry.with(context)
                        .radius(10)
                        .sampling(8)
                        .capture(holder.videoImageView).get()
                    holder.videoImageView.setImageDrawable(BitmapDrawable(bitmap))
                }, 300)

            }else{
                holder.videoImageView.setImageBitmap(convertBase64ToImage(context,documentItem.image))
            }
        }

        holder.videoImageView.setOnClickListener {
            if (documentItem.isLocked) {
                itemClickListener?.onUnlockVideoClick(position)
            }else{
                itemClickListener?.onItemClick(position)
            }
        }

        holder.itemView.setOnClickListener {
            if (documentItem.isLocked) {
                itemClickListener?.onUnlockVideoClick(position)
            }else{
                itemClickListener?.onItemClick(position)
            }
        }
    }
    private fun convertBase64ToImage(context: Context, base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        return bitmap
    }


    override fun getItemCount(): Int {
        return filteredList.size
    }
    class DocumentVideoHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoImageView: ImageView = view.findViewById(R.id.Video_thumbnail)
        val titleTextView: TextView = view.findViewById(R.id.Video_title)
        val descriptionTextView: TextView = view.findViewById(R.id.Video_description)
    }
}