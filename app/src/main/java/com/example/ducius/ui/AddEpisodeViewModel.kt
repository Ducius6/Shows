package com.example.ducius.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ducius.model.PostEpisode
import com.example.ducius.model.repository.EpisodesRepository
import com.example.ducius.responses.CompleteEpisodeResponse
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception

class AddEpisodeViewModel : ViewModel(), Observer<CompleteEpisodeResponse> {

    private val episodeLiveData = MutableLiveData<CompleteEpisodeResponse>()

    var file: File? = null

    fun saveFile(mFile: File) {
        file = mFile
    }

    val liveData: LiveData<CompleteEpisodeResponse>
        get() {
            return episodeLiveData
        }

    fun postEpisodeData(imageFile: File?, episode: PostEpisode) {
        EpisodesRepository.postMedia(imageFile, episode)
    }

    init {
        EpisodesRepository.episodeResponse().observeForever(this)
    }

    override fun onCleared() {
        EpisodesRepository.episodeResponse().removeObserver(this)
    }

    override fun onChanged(episode: CompleteEpisodeResponse?) {
        episodeLiveData.value = episode
    }

    var seasonEpisode: String? = null
    var episodeImageURi: String? = null


    fun saveSeasonAndEpisode(seasonEpisode: String) {
        this.seasonEpisode = seasonEpisode
    }

    fun saveEpisodeImage(uri: String) {
        this.episodeImageURi = uri
    }

    fun createFileFromInputStream(photoFile: File?, inputStream: InputStream?): File? {
        try {
            val outputStream = FileOutputStream(photoFile)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            bytesRead = inputStream?.read(buffer)!!
            while (bytesRead != -1) {
                outputStream.write(buffer, 0, bytesRead)
                bytesRead = inputStream.read(buffer)
            }
            outputStream.close()
            inputStream.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return photoFile
    }

    fun getImageUri(): String? = episodeImageURi
}