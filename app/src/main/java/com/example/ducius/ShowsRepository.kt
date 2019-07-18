package com.example.ducius

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object ShowsRepository {

    private val showsLiveData = MutableLiveData<List<Show>>()

    fun getShows(): LiveData<List<Show>> = showsLiveData

    private lateinit var showsList: MutableList<Show>


    init {
        showsList = mutableListOf()
        with(showsList){
            add(Show(1,"Big Bang Theory", "2007-2019", R.drawable.bigbangtheory, "Group of geeks", arrayListOf<Episode>()))
            add(Show(2,"Sherlock", "2010-", R.drawable.sherlock, "Sherloock", arrayListOf<Episode>()))
            add(Show(3,"Mentalist", "2008-2015", R.drawable.mentalist, "Mentalist is best series ever", arrayListOf<Episode>()))
            add(Show(4,"Daredevil", "2015-2018", R.drawable.daredevil, "Dareeedevil", arrayListOf<Episode>()))
            add(Show(5,"The Punisher", "2017-2019", R.drawable.punisher, "Punisher is great", arrayListOf<Episode>()))
        }
        showsLiveData.value = showsList
    }


}