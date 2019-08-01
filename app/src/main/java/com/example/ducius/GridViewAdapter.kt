package com.example.ducius

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.ducius.model.Show
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_grid_view.view.*

private const val BASE_URL = "https://api.infinum.academy"

class GridViewAdapter(private val context: Context, private val clickListener: GridViewAdapter.OnShowClicked) :
    BaseAdapter() {

    private var listOfShows = listOf<Show>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.item_grid_view, null)
        view.setOnClickListener { clickListener.onClick(listOfShows.get(position), position) }
        Picasso.get().load(BASE_URL + listOfShows.get(position).imageURL).into(view.gridItemImageView)
        view.gridItemTextView.text = listOfShows.get(position).name
        return view
    }

    override fun getItem(position: Int): Show = listOfShows.get(position)


    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = listOfShows.size


    fun setData(shows: List<Show>) {
        this.listOfShows = shows
        notifyDataSetChanged()
    }

    interface OnShowClicked {
        fun onClick(show: Show, position: Int)
    }
}