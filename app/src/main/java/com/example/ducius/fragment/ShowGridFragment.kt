package com.example.ducius.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.GridViewAdapter
import com.example.ducius.R
import com.example.ducius.model.Show
import com.example.ducius.responses.ShowsResponse
import com.example.ducius.ui.LoginActivity
import com.example.ducius.ui.ShowsViewModel
import kotlinx.android.synthetic.main.fragment_show_grid.*

class ShowGridFragment : Fragment(), GridViewAdapter.OnShowClicked {
    private lateinit var adapter: GridViewAdapter
    private lateinit var viewModel: ShowsViewModel
    private var twoPane: Boolean? = null
    private var bundle: Bundle = Bundle()
    private var firstTime: Boolean? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GridViewAdapter(requireContext(), this)
        gridView.adapter = adapter
        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)


        if (isNetworkAvailable() == true) {
            viewModel.getShowData()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.no_internet))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK), DialogInterface.OnClickListener { dialog, _ ->
                    activity?.finishAffinity()
                    dialog.cancel()
                }).show()
        }

        twoPane = arguments?.getBoolean(ShowsContainerActivity.TWO_PANE)
        firstTime = arguments?.getBoolean(ShowsContainerActivity.FIRST_TIME)

        with(bundle) {
            putBoolean(ShowsContainerActivity.TWO_PANE, twoPane!!)
            putBoolean(ShowsContainerActivity.FIRST_TIME, firstTime!!)
        }

        viewModel.liveData.observe(this, Observer {
            updateUI(it)
            if (twoPane!!) {
                bundle.putString(ShowListFragment.SHOW_ID, it.showsList?.first()?.ID)
                val fragmentDetails = ShowDetailsFragment()
                fragmentDetails.arguments = bundle
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.detailsFragmentContainer, fragmentDetails)
                    commit()
                }
            }
        })

        logOutButton.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout_question))
                .setPositiveButton(getString(R.string.OK)) { dialog, _ ->
                    dialog.dismiss()
                    viewModel.clearUsernameAndPassword()
                    activity?.finishAffinity()
                    startActivity(Intent(context, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }.show()
            dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(),R.color.pink))
            dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(),R.color.pink))
        }

        showListFloatingButton.setOnClickListener {
            with(bundle) {
                putBoolean(ShowsContainerActivity.TWO_PANE, twoPane!!)
                putBoolean(ShowsContainerActivity.FIRST_TIME, false)
            }
            val fragment = ShowListFragment()
            fragment.arguments = bundle
            if (twoPane != null) {
                if (twoPane!!) {
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.masterFragmentContainer, fragment)
                        commit()
                    }
                } else {
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.phoneFragmentContainer, fragment)
                        commit()
                    }
                }
            }
        }
    }

    private fun updateUI(showsResponse: ShowsResponse?) {
        if (showsResponse?.isSuccessful == true) {
            showsResponse.showsList?.let { adapter.setData(it) }
        } else {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(show: Show, position: Int) {
        fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val fragment = ShowDetailsFragment()
        val bundle = Bundle()
        with(bundle) {
            putString(ShowListFragment.SHOW_ID, show.ID)
            putBoolean(ShowsContainerActivity.TWO_PANE, twoPane!!)
            putBoolean(ShowsContainerActivity.FIRST_TIME, false)
        }
        fragment.arguments = bundle
        if (twoPane != null) {
            if (twoPane!!) {
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.detailsFragmentContainer, fragment)
                    commit()
                }
            } else {
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.phoneFragmentContainer, fragment)
                    addToBackStack("showDetailsFragment")
                    commit()
                }
            }
        }
    }

    private fun isNetworkAvailable():Boolean {
        val connectivityManager =  context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.getActiveNetworkInfo()
        return activeNetworkInfo != null && activeNetworkInfo.isConnected()
    }
}