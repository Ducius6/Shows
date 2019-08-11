package com.example.ducius.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ducius.R
import com.example.ducius.model.Show
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_show.view.*

private const val BASE_URL = "https://api.infinum.academy"

class ShowsAdapter(private val clickListener: OnShowClicked) :
    RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    var selectedPosition = RecyclerView.NO_POSITION

    private var listOfShows = listOf<Show>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false))
    }

    override fun getItemCount(): Int = listOfShows.size

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        with(holder.itemView) {
            showName.text = listOfShows.get(position).name
            showLikeCount.text = listOfShows.get(position).likesCount.toString()
            Picasso.get().load(BASE_URL + listOfShows.get(position).imageURL)
                .into(showImageView)
            rootView.setOnClickListener { clickListener.onClick(listOfShows.get(position), position) }
        }
        if (selectedPosition == position)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.highlightgray))
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
    }

    fun setData(shows: List<Show>) {
        this.listOfShows = shows
        notifyDataSetChanged()
    }

    inner class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnShowClicked {
        fun onClick(show: Show, position: Int)
    }
}