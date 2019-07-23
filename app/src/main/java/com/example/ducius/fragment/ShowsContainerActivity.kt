package com.example.ducius.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ducius.R
import kotlinx.android.synthetic.main.activity_shows_container.*

class ShowsContainerActivity : AppCompatActivity() {

    private var twoPane: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_container)

        if (phoneFragmentContainer != null) {
            twoPane = false
        }

        val bundle = Bundle()
        bundle.putBoolean("twopane", twoPane)
        bundle.putBoolean("firsttime", true)

        if (twoPane) {
            val fragmentMaster = ShowListFragment()
            val fragmentDetails = ShowDetailsFragment()
            fragmentMaster.arguments = bundle
            fragmentDetails.arguments = bundle
            supportFragmentManager.beginTransaction().apply {
                add(R.id.masterFragmentContainer, fragmentMaster)
                add(R.id.detailsFragmentContainer, fragmentDetails)
                commit()
            }
        } else {
            val fragment = ShowListFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().apply {
                add(R.id.phoneFragmentContainer, fragment)
                commit()
            }
        }
    }
}