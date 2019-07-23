package com.example.ducius.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R
import com.example.ducius.model.Show
import com.example.ducius.shared.gone
import com.example.ducius.ui.EpisodeAdapter
import com.example.ducius.ui.EpisodesViewModel
import kotlinx.android.synthetic.main.fragment_show_details.*

class ShowDetailsFragment : Fragment() {

    private lateinit var show: Show
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var viewModel: EpisodesViewModel
    private var twoPane: Boolean? = null
    private var firstTime: Boolean? = null

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
            show = if (firstTime!!) {
                viewModel.getFirstShow()
            } else {
                arguments?.getSerializable(ShowListFragment.SHOW) as Show
            }
        }

        twoPane.let {
            if (twoPane!!.not()) {
                setHasOptionsMenu(true)
                (activity as AppCompatActivity).setSupportActionBar(showsDetailsToolbar)
                if ((activity as AppCompatActivity).supportActionBar != null) {
                    with((activity as AppCompatActivity)){
                        supportActionBar?.setDisplayShowHomeEnabled(true)
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        supportActionBar?.title = show.name
                    }
                }
            } else if (twoPaneShowName != null) {
                twoPaneShowName.text = show.name
            }
        }

        showDesc.text = show.description
        episodeAdapter = EpisodeAdapter()
        episodesRecyclerView.adapter = episodeAdapter

        viewModel.liveData.observe(this, Observer { episodes ->
            if (episodes != null) {
                episodes.get(show.ID)?.let {
                    episodeAdapter.setData(episodes = it)
                    removeItems()
                }
            }
        })

        addEpisodeFloatingButton.setOnClickListener {
            val addEpisodeFragment = AddEpisodeFragment()
            val bundle = Bundle()
            with(bundle){
                putInt(SHOW_ID, show.ID)
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