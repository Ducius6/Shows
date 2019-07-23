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
import com.example.ducius.shared.invisible
import com.example.ducius.ui.EpisodeAdapter
import com.example.ducius.ui.EpisodesViewModel
import kotlinx.android.synthetic.main.fragment_show_details.*

class ShowDetailsFragment : Fragment() {

    private lateinit var show: Show
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var viewModel: EpisodesViewModel
    private var twoPane: Boolean? = null
    private var firstTime: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)

        twoPane = arguments?.getBoolean("twopane")
        firstTime = arguments?.getBoolean("firsttime")
        if (firstTime != null) {
            if (firstTime!!) {
                show = viewModel.getFirstShow()
            } else {
                show = arguments?.getSerializable("show") as Show
            }
        }

        twoPane.let {
            if(twoPane!!.not()){
                setHasOptionsMenu(true)
                (activity as AppCompatActivity).setSupportActionBar(showsDetailsToolbar)
                if ((activity as AppCompatActivity).supportActionBar != null) {
                    (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
                    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    (activity as AppCompatActivity).supportActionBar?.title = show.name
                }
            }else{
                showsDetailsToolbar.invisible()
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
            bundle.putInt("showID", show.ID)
            bundle.putBoolean("twopane", twoPane!!)
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