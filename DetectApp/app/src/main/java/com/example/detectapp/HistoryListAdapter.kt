package com.example.detectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryListAdapter(private val mList: MutableList<History> = ArrayList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setAllData(historyList: List<History>) {
        mList.clear()
        mList.addAll(historyList)
    }

    fun addAllData(historyList: List<History>) {
        mList.addAll(historyList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position >= mList.size) return

        val itemView = (holder as HistoryListAdapter.ViewHolder).itemView

        val history = mList[position]
        itemView.text_prediction.text = history.prediction
        Picasso.get()
            .load(history.image)
            .placeholder(R.drawable.man_1)
            .into(itemView.image_picture)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
