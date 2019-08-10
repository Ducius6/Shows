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
import com.example.ducius.model.PostComment
import com.example.ducius.responses.CommentsResponse
import com.example.ducius.shared.gone
import com.example.ducius.ui.CommentsAdapter
import com.example.ducius.ui.CommentsViewModel
import kotlinx.android.synthetic.main.fragment_comments.*

class CommentsFragment : Fragment() {

    private lateinit var viewModel: CommentsViewModel
    private var episodeId: String? = null
    private lateinit var commentAdapter: CommentsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        episodeId = arguments?.getString(ShowDetailsFragment.EPISODE_ID)
        viewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
        commentAdapter = CommentsAdapter()
        commentsRecyclerView.adapter = commentAdapter

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(commentsToolbar)
        if ((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        episodeId?.let { viewModel.getAllComments(it) }
        viewModel.commentsLiveData.observe(this, Observer {
            updateComments(it)
        })

        viewModel.commentPostLiveData.observe(this, Observer {
            commentEditText.text.clear()
            episodeId?.let { viewModel.getAllComments(it) }
        })

        refreshIcon.setOnClickListener {
            episodeId?.let { viewModel.getAllComments(it) }
            Toast.makeText(requireContext(), getString(R.string.up_to_date), Toast.LENGTH_LONG).show()
        }

        postButton.setOnClickListener {
            episodeId?.let { PostComment(commentEditText.text.toString(), it) }?.let {
                viewModel.postComment(it)
            }
        }
    }

    private fun updateComments(commentsResponse: CommentsResponse?) {
        if (commentsResponse?.isSuccessful == true) {
            commentsProgressBar.gone()
            if (commentsResponse?.listOfComments?.isNotEmpty()!!) {
                removeItems()
            }
            commentsResponse.listOfComments?.let {
                commentAdapter.setData(comments = it)
            }
        } else {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun removeItems() {
        sorryTextView.gone()
        emptyCommentPlaceholder.gone()
        addCommentTextView.gone()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}