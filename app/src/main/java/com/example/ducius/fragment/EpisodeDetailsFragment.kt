package com.example.ducius.fragment

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R
import com.example.ducius.responses.EpisodeDetailsResponse
import com.example.ducius.shared.gone
import com.example.ducius.ui.EpisodeDetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_episode_details.*

private const val BASE_URL = "https://api.infinum.academy"

class EpisodeDetailsFragment : Fragment() {

    private lateinit var viewModel: EpisodeDetailsViewModel
    private var twoPane: Boolean? = null
    private var episodeId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_episode_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(EpisodeDetailsViewModel::class.java)

        twoPane = arguments?.getBoolean(ShowsContainerActivity.TWO_PANE)
        episodeId = arguments?.getString(ShowDetailsFragment.EPISODE_ID)

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(episodeDetailsToolbar)
        if ((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar?.title = ""
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        if (isNetworkAvailable() == true) {
            episodeId?.let { viewModel.getEpisodeDetailsData(it) }
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.no_internet))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK), DialogInterface.OnClickListener { dialog, _ ->
                    activity?.finishAffinity()
                    dialog.cancel()
                }).show()
        }

        viewModel.liveData.observe(this, Observer {
            updateEpisodeDetails(it)
        })

        commentsImage.setOnClickListener {
            val fragment = CommentsFragment()
            val bundle = Bundle()
            with(bundle) {
                putBoolean(ShowsContainerActivity.TWO_PANE, twoPane!!)
                putString(ShowDetailsFragment.EPISODE_ID, episodeId)
            }
            fragment.arguments = bundle
            if (twoPane != null) {
                if (twoPane!!) {
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.detailsFragmentContainer, fragment)
                        addToBackStack("commentFragment")
                        commit()
                    }
                } else {
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.phoneFragmentContainer, fragment)
                        addToBackStack("commentFragment")
                        commit()
                    }
                }
            }
        }

        commentsText.setOnClickListener { commentsImage.performClick() }
    }

    private fun updateEpisodeDetails(episodeDetailsResponse: EpisodeDetailsResponse) {
        if (episodeDetailsResponse.isSuccessful) {
            Picasso.get().load(BASE_URL + episodeDetailsResponse.episodeDetails?.imageUrl).into(episodeDetailsImageView)
            episodeDetailsName.text = episodeDetailsResponse.episodeDetails?.title
            episodeDetailsSeasonEpisode.text = String.format(
                "S%s E%s",
                episodeDetailsResponse.episodeDetails?.season,
                episodeDetailsResponse.episodeDetails?.episodeNumber
            )
            episodeDetailsDesc.text = episodeDetailsResponse.episodeDetails?.description
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.getActiveNetworkInfo()
        return activeNetworkInfo != null && activeNetworkInfo.isConnected()
    }
}