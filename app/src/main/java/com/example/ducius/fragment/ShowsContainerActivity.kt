package com.example.ducius.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
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

        with(bundle) {
            putBoolean(TWO_PANE, twoPane)
            putBoolean(FIRST_TIME, true)
        }

        if (tabletSize) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            if (twoPane) {
                addFragmentTwoPanelTablet()
            } else {
                addFragmentSinglePanel()
            }
        } else if (savedInstanceState == null) {
            addFragmentSinglePanel()
        }
    }

    private fun addFragmentSinglePanel() {
        val fragment = ShowGridFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.phoneFragmentContainer, fragment)
            commit()
        }
    }

    private fun addFragmentTwoPanelTablet() {
        val fragmentMaster = ShowGridFragment()
        fragmentMaster.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.masterFragmentContainer, fragmentMaster)
            commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) finishAffinity()
        else super.onBackPressed()

    }
}