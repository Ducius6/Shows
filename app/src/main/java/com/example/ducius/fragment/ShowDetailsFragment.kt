package com.example.ducius.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R
import com.example.ducius.model.ShowDetails
import com.example.ducius.responses.EpisodeResponse
import com.example.ducius.responses.ShowDetailsResponse
import com.example.ducius.shared.gone
import com.example.ducius.ui.EpisodeAdapter
import com.example.ducius.ui.EpisodesViewModel
import kotlinx.android.synthetic.main.fragment_show_details.*

class ShowDetailsFragment : Fragment() {

    private var showDetails: ShowDetails? = null
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var viewModel: EpisodesViewModel
    private var twoPane: Boolean? = null
    private var firstTime: Boolean? = null
    private lateinit var showId: String

    companion object {
        const val SHOW_ID = "showID"
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

        episodeAdapter = EpisodeAdapter()
        episodesRecyclerView.adapter = episodeAdapter

        viewModel.getEpisodeData(showId)
        viewModel.episodeLiveData.observe(this, Observer {
            updateEpisodes(it)
        })

        viewModel.getShowDetailsLiveData(showId)
        viewModel.detailsLiveData.observe(this, Observer {
            updateShowDetails(it)
        })

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

    private fun updateShowDetails(showDetailsResponse: ShowDetailsResponse?) {
        if (showDetailsResponse?.isSuccessful == true){
            (activity as AppCompatActivity).supportActionBar?.title = showDetailsResponse.show?.name
            showDesc.text = showDetailsResponse.show?.description
            showDetails = showDetailsResponse.show
            if(twoPaneShowName != null){
                twoPaneShowName.text = showDetailsResponse.show?.name
            }
        }
        removeItems()
    }

    private fun updateEpisodes(episodeResponse: EpisodeResponse?) {
        if (episodeResponse?.isSuccessful == true) {
            episodeResponse.listOfEpisodes?.let { episodeAdapter.setData(it) }
        } else {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
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