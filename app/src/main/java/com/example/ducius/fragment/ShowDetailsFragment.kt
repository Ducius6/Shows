package com.example.ducius.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R
import com.example.ducius.model.CompleteShow
import com.example.ducius.model.Episode
import com.example.ducius.model.ShowDetails
import com.example.ducius.responses.LikeResponse
import com.example.ducius.shared.gone
import com.example.ducius.ui.EpisodeAdapter
import com.example.ducius.ui.EpisodesViewModel
import kotlinx.android.synthetic.main.fragment_show_details.*

class ShowDetailsFragment : Fragment(), EpisodeAdapter.OnEpisodeClicked {

    private var showDetails: ShowDetails? = null
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var viewModel: EpisodesViewModel
    private var twoPane: Boolean? = null
    private var firstTime: Boolean? = null
    private lateinit var showId: String

    companion object {
        const val SHOW_ID = "showID"
        const val EPISODE_ID = "episodeID"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)

        twoPane = arguments?.getBoolean(ShowsContainerActivity.TWO_PANE)
        firstTime = arguments?.getBoolean(ShowsContainerActivity.FIRST_TIME)
        if (firstTime != null) {
            if (firstTime!!) {
                showId = arguments?.getString(ShowListFragment.SHOW_ID).toString()
            } else {
                showId = arguments?.getString(ShowListFragment.SHOW_ID).toString()
            }
        }

        twoPane.let {
            if (twoPane!!.not()) {
                setHasOptionsMenu(true)
                (activity as AppCompatActivity).setSupportActionBar(showsDetailsToolbar)
                if ((activity as AppCompatActivity).supportActionBar != null) {
                    with((activity as AppCompatActivity)) {
                        supportActionBar?.setDisplayShowHomeEnabled(true)
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    }
                }
            }
        }

        getLikeAndDislike()

        episodeAdapter = EpisodeAdapter(this)
        episodesRecyclerView.adapter = episodeAdapter

        viewModel.getCompleteShow(showId)
        viewModel.completeShowLiveData.observe(this, Observer {
            updateShowAndEpisode(it)
        })

        viewModel.likeLiveData.observe(this, Observer {
            if (it.isSuccessful && showId == it.id) {
                countTextView.text = it.likesCount.toString()
                viewModel.saveLikesCount(showId, it.likesCount!!)
            }
        })

        likeImageView.setOnClickListener {
            likeImageView.setBackgroundResource(R.drawable.circle_pink)
            viewModel.saveLike(showId)
            likeImageView.isEnabled = false
            dislikeImageView.isEnabled = false
            viewModel.likeShow(showId)
        }

        dislikeImageView.setOnClickListener {
            dislikeImageView.setBackgroundResource(R.drawable.circle_pink)
            viewModel.saveDislike(showId)
            dislikeImageView.isEnabled = false
            likeImageView.isEnabled = false
            viewModel.dislikeShow(showId)
        }

        addEpisodeFloatingButton.setOnClickListener {
            val addEpisodeFragment = AddEpisodeFragment()
            val bundle = Bundle()
            with(bundle) {
                showDetails?.ID?.let { it1 -> putString(SHOW_ID, it1) }
                putBoolean(ShowsContainerActivity.TWO_PANE, twoPane!!)
            }
            addEpisodeFragment.arguments = bundle
            if (twoPane != null) {
                if (twoPane!!) {
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.detailsFragmentContainer, addEpisodeFragment)
                        addToBackStack("addEpisodeFragment")
                        commit()
                    }
                } else {
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.phoneFragmentContainer, addEpisodeFragment)
                        addToBackStack("addEpisodeFragment")
                        commit()
                    }
                }
            }
        }

        addSomeEpisodes.setOnClickListener {
            addEpisodeFloatingButton.performClick()
        }
    }

    private fun getLikeAndDislike() {
        val likeCount = viewModel.getLikeCount(showId)
        val liked = viewModel.getLikeorDislike(showId)
        if (likeCount != -1000) {
            countTextView.text = likeCount.toString()
        }
        if (liked == 1) {
            likeImageView.setBackgroundResource(R.drawable.circle_pink)
            likeImageView.isEnabled = false
            dislikeImageView.isEnabled = false
        } else if (liked == -1) {
            dislikeImageView.setBackgroundResource(R.drawable.circle_pink)
            likeImageView.isEnabled = false
            dislikeImageView.isEnabled = false
        }
    }

    private fun updateShowAndEpisode(completeShow: CompleteShow) {
        if (completeShow.isSuccessful) {
            showDetailsProgressBar.gone()
            (activity as AppCompatActivity).supportActionBar?.title = completeShow.showDetailsResponse?.show?.name
            showDesc.text = completeShow.showDetailsResponse?.show?.description
            showDetails = completeShow.showDetailsResponse?.show
            if (twoPaneShowName != null) {
                twoPaneShowName.text = completeShow.showDetailsResponse?.show?.name
            }
            completeShow.episodeResponse?.listOfEpisodes?.let { episodeAdapter.setData(it) }
            removeItems()
        } else {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(episode: Episode) {
        val fragment = EpisodeDetailsFragment()
        val bundle = Bundle()
        with(bundle) {
            putString(EPISODE_ID, episode.id)
            putBoolean(ShowsContainerActivity.TWO_PANE, twoPane!!)
        }
        fragment.arguments = bundle
        if (twoPane != null) {
            if (twoPane!!) {
                fragmentManager?.beginTransaction()?.apply {
                    addToBackStack("episodeDetailsFragment")
                    replace(R.id.detailsFragmentContainer, fragment)
                    commit()
                }
            } else {
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.phoneFragmentContainer, fragment)
                    addToBackStack("episodeDetailsFragment")
                    commit()
                }
            }
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
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}