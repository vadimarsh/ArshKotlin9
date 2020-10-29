package com.example.arshkotlin9.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arshkotlin9.dto.PostModel
import com.example.arshkotlin9.verboseTime
import kotlinx.android.synthetic.main.layout_post.view.authorTv
import kotlinx.android.synthetic.main.layout_post.view.contentTv
import kotlinx.android.synthetic.main.layout_repost.view.*
import kotlinx.android.synthetic.main.layout_repost.view.createdTv

class RepostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.authorName
            contentTv.text = post.content
            srcAuthorTv.text = post.source!!.authorName
            srcContentTv.text = post.source!!.content
            createdTv.text = verboseTime(post.created)
            srcCreatedTv.text = verboseTime(post.source!!.created)
        }
    }
}