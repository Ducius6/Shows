package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.PostComment
import com.example.ducius.model.repository.EpisodesRepository
import com.example.ducius.responses.CommentsResponse
import com.example.ducius.responses.PostCommentResponse

class CommentsViewModel : ViewModel(), Observer<Any> {

    private val postCommentLiveData = MutableLiveData<PostCommentResponse>()

    private val commentLiveData = MutableLiveData<CommentsResponse>()

    val commentPostLiveData: LiveData<PostCommentResponse>
        get() {
            return postCommentLiveData
        }

    val commentsLiveData: LiveData<CommentsResponse>
        get() {
            return commentLiveData
        }

    fun postComment(comment: PostComment) {
        EpisodesRepository.postComment(comment)
    }

    fun getAllComments(episodeId: String) {
        EpisodesRepository.getComments(episodeId)
    }

    init {
        EpisodesRepository.postedCommentLiveData().observeForever(this)
        EpisodesRepository.commentsLiveData().observeForever(this)
    }

    override fun onCleared() {
        EpisodesRepository.postedCommentLiveData().removeObserver(this)
        EpisodesRepository.commentsLiveData().removeObserver(this)
    }


    override fun onChanged(any: Any?) {
        if (any is CommentsResponse) {
            commentLiveData.value = any
        } else if (any is PostCommentResponse) {
            postCommentLiveData.value = any
        }
    }

}