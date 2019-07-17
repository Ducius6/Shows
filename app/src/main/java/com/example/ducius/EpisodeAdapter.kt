package com.example.ducius

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodeAdapter(private val listOfEpisodes: List<Episode>) :
    RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeAdapter.EpisodeViewHolder {
        return EpisodeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false))
    }

    override fun getItemCount(): Int = listOfEpisodes.size

    override fun onBindViewHolder(holder: EpisodeAdapter.EpisodeViewHolder, position: Int) {
        with(holder.itemView) {
            episodeName.text = listOfEpisodes.get(position).title
            seasonEpisodeNumber.text = listOfEpisodes.get(position).seasonEpisode
        }
    }

    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}