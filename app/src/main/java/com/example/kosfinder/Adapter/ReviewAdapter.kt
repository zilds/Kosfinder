package com.example.kosfinder.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kosfinder.model.Review
import com.example.kosfinder.R

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private val reviewList = ArrayList<Review>()

    fun setData(data: List<Review>) {
        reviewList.clear()
        reviewList.addAll(data)
        notifyDataSetChanged()
    }

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNamaReviewer: TextView = itemView.findViewById(R.id.txtNamaReviewer)
        val txtRatingReview: TextView = itemView.findViewById(R.id.txtRatingReview)
        val txtKomentarReview: TextView = itemView.findViewById(R.id.txtKomentarReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ulasan, parent, false)

        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]

        holder.txtNamaReviewer.text = review.user?.name ?: "User"
        holder.txtRatingReview.text = "⭐ ${review.rating}.0"
        holder.txtKomentarReview.text = review.komentar ?: "-"
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}