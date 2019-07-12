package com.example.ducius

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_shows_details.*

class ShowsDetailsActivity : AppCompatActivity() {

    companion object {
        const val SHOW_REFERENCE = "USERNAME"
        const val REQUEST_CODE = 99

        fun newInstance(context: Context, show: Show): Intent =
            Intent(context, ShowsDetailsActivity::class.java).putExtra(SHOW_REFERENCE, show)
    }

    lateinit var show: Show
    lateinit var episodeAdapter: EpisodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_details)

        show = intent.getSerializableExtra(SHOW_REFERENCE) as Show
        setSupportActionBar(showsDetailsToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = show!!.name
        }

        showDesc.text = show.description
        episodeAdapter = EpisodeAdapter(show.listOfEpisodes)
        episodesRecyclerView.layoutManager = LinearLayoutManager(this)
        episodesRecyclerView.adapter = episodeAdapter
        if (show.listOfEpisodes.isEmpty()) {
            sleepyIcon.visibility = View.VISIBLE
            asleepTextView.visibility = View.VISIBLE
        } else {
            sleepyIcon.visibility = View.INVISIBLE
        }

        addEpisodeFloatingButton.setOnClickListener {
            startActivityForResult(Intent(this, AddEpisodeActivity::class.java), REQUEST_CODE)
        }

        addSomeEpisodes.setOnClickListener {
            addEpisodeFloatingButton.performClick()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    show.listOfEpisodes.add(
                        Episode(
                            data.getStringExtra(AddEpisodeActivity.EPISODE_TITLE),
                            data.getStringExtra(AddEpisodeActivity.EPISODE_DESC)
                        )
                    )
                    sleepyIcon.visibility = View.INVISIBLE
                    asleepTextView.visibility = View.INVISIBLE
                    wakeUpTextView.visibility = View.INVISIBLE
                    addSomeEpisodes.visibility = View.INVISIBLE
                    episodeAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
