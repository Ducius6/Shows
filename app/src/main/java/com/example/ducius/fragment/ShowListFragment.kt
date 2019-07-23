package com.example.ducius.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShowsAdapter(this)
        showsRecyclerView.adapter = adapter
        twoPane = arguments?.getBoolean("twopane")

        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        viewModel.liveData.observe(this, Observer { shows ->
            if (shows != null) {
                adapter.setData(shows)
            }
        })
    }

    override fun onClick(show: Show) {
        val fragment = ShowDetailsFragment()
        val bundle = Bundle()
        bundle.putSerializable("show", show)
        bundle.putBoolean("twopane", twoPane!!)
        bundle.putBoolean("firsttime", false)
        fragment.arguments = bundle
        if(twoPane != null){
            if(twoPane!!){
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.detailsFragmentContainer, fragment)
                    addToBackStack("showDetailsFragment")
                    commit()
                }
            }else{
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.phoneFragmentContainer, fragment)
                    addToBackStack("showDetailsFragment")
                    commit()
                }
            }
        }
    }
}