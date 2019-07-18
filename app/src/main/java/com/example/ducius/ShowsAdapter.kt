package com.example.ducius

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_show.view.*

class ShowsAdapter(private val clickListener: OnShowClicked) :
    RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    private var listOfShows = listOf<Show>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsAdapter.ShowViewHolder {
        return ShowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false))
    }

    override fun getItemCount(): Int = listOfShows.size

    override fun onBindViewHolder(holder: ShowsAdapter.ShowViewHolder, position: Int) {
        with(holder.itemView) {
            showName.text = listOfShows.get(position).name
            showImageView.setImageResource(listOfShows.get(position).imageId)
            showAirDate.text = listOfShows.get(position).airDate
            rootView.setOnClickListener { clickListener.onClick(listOfShows.get(position)) }
        }
    }

    fun setData(shows: List<Show>) {
        this.listOfShows = shows
        notifyDataSetChanged()
    }

    inner class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnShowClicked {
        fun onClick(show: Show)
    }
}