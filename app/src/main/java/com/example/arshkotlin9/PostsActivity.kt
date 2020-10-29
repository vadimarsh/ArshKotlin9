package com.example.arshkotlin9

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arshkotlin9.adapter.PostAdapter
import com.example.arshkotlin9.dto.PostModel
import kotlinx.android.synthetic.main.activity_posts.*
import kotlinx.android.synthetic.main.layout_footer.*
import kotlinx.coroutines.launch
import retrofit2.Response
import splitties.activities.start
import splitties.toast.toast

class PostsActivity : AppCompatActivity(), PostAdapter.OnLikeBtnClickListener,
    PostAdapter.OnRepostBtnClickListener, PostAdapter.OnLoadMoreBtnClickListener {
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        swipeLayout.setOnRefreshListener { refreshPosts() }
    }

    private fun refreshPosts() {
        lifecycleScope.launch {
            val newPostsResponse = App.repository.getPostsRecent()
            swipeLayout.isRefreshing = false
            if (newPostsResponse.isSuccessful) {
                val adap = recView.adapter as PostAdapter
                adap?.loadNewItems((newPostsResponse.body() as MutableList<PostModel>?)!!)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fab.setOnClickListener(View.OnClickListener { start<NewPostActivity>() })
        lifecycleScope.launch {
            dialog = ProgressDialog(this@PostsActivity).apply {
                setMessage(this@PostsActivity.getString(R.string.please_wait))
                setTitle(R.string.loading_posts)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val result: Response<List<PostModel>> = App.repository.getPostsRecent()
            //println(result.body())
            dialog?.dismiss()
            if (result.isSuccessful) {
                recView.apply {
                    layoutManager = LinearLayoutManager(this@PostsActivity)
                    adapter = PostAdapter(result.body() as MutableList<PostModel>).apply {
                        likeBtnClickListener = this@PostsActivity
                        commentBtnClickListener = this@PostsActivity
                        loadMoreBtnClickListener = this@PostsActivity
                    }
                }
            } else {
                toast(R.string.msg_auth_err)
            }
        }
    }

    override fun onLikeBtnClicked(item: PostModel, position: Int) {

        lifecycleScope.launch {
            item.likeActionPerforming = true
            with(recView) {
                adapter?.notifyItemChanged(position)
                val response = if (item.likedByMe) {
                    App.repository.dislikePost(item.id)
                } else {
                    App.repository.likePost(item.id)
                }
                item.likeActionPerforming = false
                if (response.isSuccessful) {
                    item.updateLikes(response.body()!!)
                }
                adapter?.notifyItemChanged(position)

            }
        }
    }

    override fun onRepostBtnClicked(item: PostModel, position: Int) {
        if (item.repostedByMe == false) {
            start<NewPostActivity> {
                putExtra("id", item.id)
            }
        } else {
            toast(getString(R.string.msg_already_reposted))
        }

    }

    override fun onLoadMoreBtnClickListener(last: Long, size: Int) {

        lifecycleScope.launch {
            val response =
                App.repository.getPostsBefore(last)
            progressBar.visibility = View.INVISIBLE
            loadMoreButton.isEnabled = true

            if (response.isSuccessful) {
                val newItems = response.body() as MutableList<PostModel>
                print(response.body())
                with(recView) {
                    val adap = adapter as PostAdapter
                    adap.refreshItems(newItems)
                    adap.notifyItemRangeInserted(size + newItems.size, newItems.size)
                }

            }
        }
    }
}
