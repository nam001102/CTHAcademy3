package com.cthacademy.android.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.R

class GridNewLessonVideoAdapter(
    private var pages: List<Int>
) : RecyclerView.Adapter<GridNewLessonVideoAdapter.PagesViewHolder>() {

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    interface OnClickListener {
        fun onButtonClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_lesson_videos_pages, parent, false)
        return PagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagesViewHolder, position: Int) {
        val page = pages[position]
        holder.pageTextView.text = page.toString()
        holder.pageCardView.setCardBackgroundColor(Color.WHITE)
        holder.pageCardView.setOnClickListener {
            onClickListener?.onButtonClick(position)
        }
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    fun getCurrentPage(): Int {
        return pages.lastOrNull() ?: 1
    }

    private fun generatePagesList(start: Int, end: Int): List<Int> {
        val pages = mutableListOf<Int>()
        for (i in start..end) {
            pages.add(i)
        }
        return pages
    }


    inner class PagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pageTextView: TextView = view.findViewById(R.id.itemTextView)
        val pageCardView: CardView = view.findViewById(R.id.itemCardView)
        val pageitemSquareRelativeView: RelativeLayout = view.findViewById(R.id.itemSquareRelativeView)
    }
}
