package com.example.ducius.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ducius.R
import com.example.ducius.model.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private var listOfComments = listOf<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))
    }

    override fun getItemCount(): Int = listOfComments.size


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        with(holder.itemView) {
            commentEmailTextView.text = listOfComments.get(position).userEmail
            commentCompleteText.text = listOfComments.get(position).text
        }
    }

    fun setData(comments: List<Comment>) {
        this.listOfComments = comments
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}