package com.example.arshkotlin9.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.arshkotlin9.R
import com.example.arshkotlin9.dto.PostModel
import com.example.arshkotlin9.verboseTime
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.layout_post.view.*
import splitties.activities.startActivity
import splitties.toast.toast

class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeButton.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        context.toast(context.getString(R.string.like_in_progress))
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
            shareButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    itemView.context.startActivity(Intent.ACTION_SEND) {
                        putExtra(
                            Intent.EXTRA_TEXT, """
                                ${item.authorName} 
                                (${item.created})
                                ${item.content}
                            """.trimIndent()
                        )
                        type = "text/plain"
                    }
                }
            }
            commentButton.setOnClickListener {

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    showDialog(context) {
                        val item = adapter.list[adapterPosition]
                        if (item.likeActionPerforming) {
                            context.toast(context.getString(R.string.repost_in_progress))
                        } else {
                            adapter.commentBtnClickListener?.onRepostBtnClicked(
                                item,
                                adapterPosition
                            )
                        }
                    }
                }
            }
        }
    }

    fun showDialog(context: Context, createBtnClicked: (content: String) -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.activity_new_post)
            .show()
        dialog.createPostBtn.setOnClickListener {
            createBtnClicked(dialog.contentEdt.text.toString())
            dialog.dismiss()
        }

    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.authorName
            contentTv.text = post.content
            likeTv.text = post.likes.toString()
            createdTv.text = verboseTime(post.created)
            commentTv.text = post.reposts.toString()

            when {
                post.likeActionPerforming -> {
                    likeButton.setImageResource(R.drawable.like_processing)
                }
                else -> {
                    if (post.likedByMe) {
                        likeButton.setImageResource(R.drawable.like_active)
                        likeTv.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorRed
                            )
                        )
                    } else {
                        likeButton.setImageResource(R.drawable.like_inactive)
                        likeTv.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorGrey
                            )
                        )
                    }
                }
            }
            when {
                post.repostActionPerforming -> {
                    commentButton.setImageResource(R.drawable.comment_pending)
                }
                else -> {
                    if (post.repostedByMe) {
                        commentButton.setImageResource(R.drawable.comment_active)
                        commentTv.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorRed
                            )
                        )
                    } else {
                        commentButton.setImageResource(R.drawable.comment)
                        commentTv.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorGrey
                            )
                        )
                    }
                }
            }
        }
    }
}