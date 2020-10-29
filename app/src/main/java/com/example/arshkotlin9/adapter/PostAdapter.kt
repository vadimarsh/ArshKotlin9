package com.example.arshkotlin9.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arshkotlin9.R
import com.example.arshkotlin9.dto.PostModel
import com.example.arshkotlin9.dto.PostTypes

class PostAdapter(val list: MutableList<PostModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var commentBtnClickListener: OnRepostBtnClickListener? = null
    var loadMoreBtnClickListener: OnLoadMoreBtnClickListener? = null
    private val ITEM_TYPE_POST = 1
    private val ITEM_TYPE_REPOST = 2
    private val ITEM_TYPE_FOOTER = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_POST -> {
                val postView =
                    LayoutInflater.from(parent.context).inflate(R.layout.layout_post, parent, false)
                PostViewHolder(this, postView)
            }
            ITEM_TYPE_REPOST -> {
                val postView =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_repost, parent, false)
                RepostViewHolder(this, postView)
            }
            else -> {
                val postView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_footer, parent, false)
                FooterViewHolder(this, postView)
            }
        }
    }


    override fun getItemCount() = list.size + 1

    override fun getItemViewType(position: Int): Int {
        return when {
            position == list.size -> ITEM_TYPE_FOOTER
            list[position].type == PostTypes.POSTBASIC -> ITEM_TYPE_POST
            else -> ITEM_TYPE_REPOST
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(list[position])
            is RepostViewHolder -> holder.bind(list[position])
            else -> Unit
        }
    }

    fun refreshItems(posts: MutableList<PostModel>) {
        list.addAll(posts)
        notifyDataSetChanged()
    }

    fun loadNewItems(posts: MutableList<PostModel>) {
        list.clear()
        list.addAll(posts)
        notifyDataSetChanged()
    }

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnRepostBtnClickListener {
        fun onRepostBtnClicked(item: PostModel, position: Int)
    }

    interface OnLoadMoreBtnClickListener {
        fun onLoadMoreBtnClickListener(last: Long, size: Int)
    }
}

