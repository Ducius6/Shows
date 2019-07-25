package com.example.ducius.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R
import com.example.ducius.model.Show
import com.example.ducius.ui.ShowsAdapter
import com.example.ducius.ui.ShowsViewModel
import kotlinx.android.synthetic.main.fragment_show_list.*

class ShowListFragment : Fragment(), ShowsAdapter.OnShowClicked {

    private lateinit var viewModel: ShowsViewModel
    private lateinit var adapter: ShowsAdapter
    private var twoPane: Boolean? = null

    companion object {
        const val SHOW = "show"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShowsAdapter(this, requireContext())
        showsRecyclerView.adapter = adapter
        twoPane = arguments?.getBoolean(ShowsContainerActivity.TWO_PANE)

        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        viewModel.liveData.observe(this, Observer { shows ->
            if (shows != null) {
                adapter.setData(shows)
            }
        })
    }

    override fun onClick(show: Show, position: Int) {
        fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        adapter.selectedPosition = position
        adapter.notifyDataSetChanged()
        val fragment = ShowDetailsFragment()
        val bundle = Bundle()
        with(bundle) {
            putSerializable(SHOW, show)
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
}