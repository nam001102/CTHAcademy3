package com.cthacademy.android.adapter

import com.cthacademy.android.modal.LessonVideoModal
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayInputStream

class LessonVideosAdapter (private val documentList: MutableList<LessonVideoModal>, private val context: Context, private val isAdmin: Boolean) : RecyclerView.Adapter<LessonVideosAdapter.DocumentVideoHolder>() {


    private var itemClickListener: OnItemClickListener? = null
    private var filteredList: List<LessonVideoModal> = documentList

    private val displayLimit = 12
    fun clearData() {
        documentList.clear()
        notifyDataSetChanged()
    }
    fun updateData(newData: List<LessonVideoModal>) {
        filteredList = newData
    }
    override fun getItemCount(): Int {
        return minOf(filteredList.size, displayLimit) // Return the minimum of data size and display limit
    }

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
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(documentItem.userPhone)
            .get()
            .addOnSuccessListener{ doc ->
                if (doc.exists()) {
                    val videoUploader = doc.getString("name")
                    holder.userTextView.text = videoUploader
                    holder.titleTextView.text = documentItem.title
                    holder.descriptionTextView.text = documentItem.description
                    val videoUri = documentItem.uri

                    val storageRef = FirebaseStorage.getInstance().reference
                    val fileReference = storageRef.child(videoUri)

                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        // Use the download URL (URI) here
                        val downloadUri = uri.toString()
                        if (documentItem.isLocked){
                            holder.videoImageView.setBackgroundColor(Color.parseColor("#D6D0D0"))
                            holder.videoLock.setImageResource(R.drawable.ic_lock_dark)
                            holder.videoLockPoint.text = documentItem.point.toString()


                        }else{
                            holder.videoLock.visibility = View.GONE
                            holder.videoLockPoint.visibility = View.GONE
                            holder.videoImageView.setImageBitmap(convertBase64ToImage(context,documentItem.image))
                        }
                    }

                    holder.itemView.setOnClickListener {
                        if (!isAdmin){
                            if (documentItem.isLocked) {
                                itemClickListener?.onUnlockVideoClick(position)
                            }else{
                                itemClickListener?.onItemClick(position)
                            }
                        }else{
                            itemClickListener?.onItemClick(position)
                        }
                    }
                }
            }

    }
    private fun convertBase64ToImage(context: Context, base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)

        return BitmapFactory.decodeStream(inputStream)
    }

    class DocumentVideoHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoImageView: ImageView = view.findViewById(R.id.Video_thumbnail)
        val videoLock: ImageView = view.findViewById(R.id.Video_lock)
        val videoLockPoint: TextView = view.findViewById(R.id.Video_lock_point)
        val titleTextView: TextView = view.findViewById(R.id.Video_title)
        val userTextView: TextView = view.findViewById(R.id.Video_uploader)
        val descriptionTextView: TextView = view.findViewById(R.id.Video_description)
    }
}