package com.cthacademy.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.R
import com.cthacademy.android.modal.BookModal


class GridShopAdapter(private val books: List<BookModal>) :
    RecyclerView.Adapter<GridShopAdapter.BookViewHolder>() {

    private var bookClickListener: BookClickListener? = null

    interface BookClickListener {
        fun onBookClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ic_book, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val data = books[position]
        // Set the data to your views in the ViewHolder
        holder.imageView.setImageResource(data.Image)
        holder.nameImageView.setImageResource(data.ImageText)

        // Set click listener
        holder.itemView.setOnClickListener {
            bookClickListener?.onBookClick(position)
        }
    }

    fun setBookClickListener(listener: BookClickListener) {
        bookClickListener = listener
    }

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.Book_image)
        val nameImageView: ImageView = view.findViewById(R.id.Book_image_text)
    }
}
