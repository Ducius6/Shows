package com.example.ducius.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ducius.R
import kotlinx.android.synthetic.main.activity_shows_container.*


class ShowsContainerActivity : AppCompatActivity() {

    private var twoPane: Boolean = true
    private val bundle = Bundle()

    companion object {
        const val TWO_PANE = "twopane"
        const val FIRST_TIME = "firsttime"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_container)

        val tabletSize = resources.getBoolean(R.bool.isTablet)

        if (phoneFragmentContainer != null) {
            twoPane = false
        }

        with(bundle){
            putBoolean(TWO_PANE, twoPane)
            putBoolean(FIRST_TIME, true)
        }

        if (tabletSize) {
            if (twoPane) {
                addFragmentTwoPanelTablet()
            } else {
                addFragmentSinglePanelTablet()
            }
        } else if (savedInstanceState == null) {
            addFragmentPhone()
        }
    }

    private fun addFragmentPhone() {
        val fragment = ShowListFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            add(R.id.phoneFragmentContainer, fragment)
            commit()
        }
    }

    private fun addFragmentSinglePanelTablet() {
        val fragment = ShowListFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.phoneFragmentContainer, fragment)
            commit()
        }
    }

    private fun addFragmentTwoPanelTablet() {
        val fragmentMaster = ShowListFragment()
        val fragmentDetails = ShowDetailsFragment()
        fragmentMaster.arguments = bundle
        fragmentDetails.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.masterFragmentContainer, fragmentMaster)
            replace(R.id.detailsFragmentContainer, fragmentDetails)
            commit()
        }
    }
}