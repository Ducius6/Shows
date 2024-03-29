package com.example.ducius.oldstuff

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R
import com.example.ducius.model.Show
import com.example.ducius.ui.ShowsAdapter
import com.example.ducius.ui.ShowsViewModel
import kotlinx.android.synthetic.main.activity_shows.*

class ShowsActivity : AppCompatActivity(), ShowsAdapter.OnShowClicked {

    private lateinit var viewModel: ShowsViewModel
    private lateinit var adapter: ShowsAdapter

    companion object {
        fun newInstance(context: Context): Intent =
            Intent(context, ShowsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        adapter = ShowsAdapter(this)
        showsRecyclerView.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        viewModel.liveData.observe(this, Observer { showsResponse ->
            if (showsResponse != null) {
                showsResponse.showsList?.let { adapter.setData(it) }
            }
        })
    }

    override fun onClick(show: Show, position: Int) = startActivity(
        ShowsDetailsActivity.newInstance(
            this,
            show
        )
    )
}
