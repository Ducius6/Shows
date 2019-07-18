package com.example.ducius

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodeAdapter :
    RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private var listOfEpisodes = listOf<Episode>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeAdapter.EpisodeViewHolder {
        return EpisodeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false))
    }

    override fun getItemCount(): Int = listOfEpisodes.size

    fun setData(episodes: List<Episode>) {
        this.listOfEpisodes = episodes
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EpisodeAdapter.EpisodeViewHolder, position: Int) {
        with(holder.itemView) {
            episodeName.text = listOfEpisodes.get(position).title
            seasonEpisodeNumber.text = listOfEpisodes.get(position).seasonEpisode
        }
    }

    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}