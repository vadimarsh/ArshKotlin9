package com.example.arshkotlin9.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_footer.view.*

class FooterViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            loadMoreButton.setOnClickListener {
                loadMoreButton.isEnabled = false
                progressBar.visibility = View.VISIBLE
                adapter.loadMoreBtnClickListener?.onLoadMoreBtnClickListener(
                    adapter.list[adapter.list.size - 1].id.toLong(),
                    adapter.list.size - 1
                )
            }
        }
    }
}