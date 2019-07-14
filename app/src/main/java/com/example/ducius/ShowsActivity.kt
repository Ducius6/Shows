package com.example.ducius

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_shows.*

class ShowsActivity : AppCompatActivity(), ShowsAdapter.OnShowClicked {

    val listOfShows = arrayListOf<Show>()

    companion object {
        fun newInstance(context: Context): Intent =
            Intent(context, ShowsActivity::class.java)
    }

    init {
        with(listOfShows) {
            add(Show(1,"Big Bang Theory", "2007-2019", R.drawable.bigbangtheory, "Group of geeks", arrayListOf<Episode>()))
            add(Show(2,"Sherlock", "2010-", R.drawable.sherlock, "Sherloock", arrayListOf<Episode>()))
            add(Show(3,"Mentalist", "2008-2015", R.drawable.mentalist, "Mentalist is best series ever", arrayListOf<Episode>()))
            add(Show(4,"Daredevil", "2015-2018", R.drawable.daredevil, "Dareeedevil", arrayListOf<Episode>()))
            add(Show(5,"The Punisher", "2017-2019", R.drawable.punisher, "Punisher is great", arrayListOf<Episode>()))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        showsRecyclerView.adapter = ShowsAdapter(listOfShows, this)
    }

    override fun OnClick(show: Show) = startActivity(ShowsDetailsActivity.newInstance(this, show))

}
