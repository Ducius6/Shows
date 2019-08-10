package com.example.ducius.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ducius.R
import com.example.ducius.model.Episode
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodeAdapter(private val clickListener: OnEpisodeClicked) :
    RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private var listOfEpisodes = listOf<Episode>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false))
    }

    override fun getItemCount(): Int = listOfEpisodes.size

    fun setData(episodes: List<Episode>) {
        this.listOfEpisodes = episodes
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        with(holder.itemView) {
            episodeName.text = listOfEpisodes.get(position).title
            seasonEpisodeNumber.text = String.format(
                "S %s E %s",
                listOfEpisodes.get(position).season,
                listOfEpisodes.get(position).episodeNumber,
                rootView.setOnClickListener { clickListener.onClick(listOfEpisodes.get(position)) }
            )
        }
    }

    interface OnEpisodeClicked {
        fun onClick(episode: Episode)
    }

    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}