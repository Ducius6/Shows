package com.example.ducius.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R
import com.example.ducius.model.Show
import com.example.ducius.shared.gone
import kotlinx.android.synthetic.main.activity_shows_details.*

class ShowsDetailsActivity : AppCompatActivity() {

    companion object {
        const val SHOW_REFERENCE = "show_reference"

        fun newInstance(context: Context, show: Show): Intent =
            Intent(context, ShowsDetailsActivity::class.java).putExtra(SHOW_REFERENCE, show)
    }

    private lateinit var show: Show
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var viewModel: EpisodesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_details)

        show = intent.getSerializableExtra(SHOW_REFERENCE) as Show
        setSupportActionBar(showsDetailsToolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = show.name
        }

        showDesc.text = show.description
        episodeAdapter = EpisodeAdapter()
        episodesRecyclerView.adapter = episodeAdapter

        viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
        viewModel.liveData.observe(this, Observer { episodes ->
            if (episodes != null) {
                episodes.get(show.ID)?.let {
                    episodeAdapter.setData(episodes = it)
                    removeItems()
                }
            }
        })

        addEpisodeFloatingButton.setOnClickListener {
            startActivity(AddEpisodeActivity.newInstance(this, show.ID))
        }

        addSomeEpisodes.setOnClickListener {
            addEpisodeFloatingButton.performClick()
        }
    }

    private fun removeItems() {
        sleepyIcon.gone()
        asleepTextView.gone()
        wakeUpTextView.gone()
        addSomeEpisodes.gone()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
