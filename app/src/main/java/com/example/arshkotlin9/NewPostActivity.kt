package com.example.arshkotlin9

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.arshkotlin9.dto.PostModel
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.coroutines.launch
import retrofit2.Response
import splitties.toast.toast
import java.io.IOException

class NewPostActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    private var idSource: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        if (intent.extras != null) {
            idSource = intent.extras!!.getLong("id")
        }
        createPostBtn.setOnClickListener {
            lifecycleScope.launch {


                // Показываем крутилку
                dialog = ProgressDialog(this@NewPostActivity).apply {
                    setMessage(this@NewPostActivity.getString(R.string.please_wait))
                    setTitle(R.string.create_new_post)
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                // Обворачиваем в try catch, потому что возможны ошибки при соединении с сетью
                try {
                    val result: Response<PostModel>
                    if (idSource != -1L) {
                        result = App.repository.sharePost(idSource, contentEdt.text.toString())
                    } else {
                        result = App.repository.addNewPost(contentEdt.text.toString())
                    }
                    if (result.isSuccessful) {
                        handleSuccessfullResult()
                    } else {
                        handleFailedResult()
                    }
                } catch (e: IOException) {
                    // обрабатываем ошибку
                    handleFailedResult()
                } finally {
                    // закрываем диалог
                    dialog?.dismiss()
                }

            }
        }
    }

    private fun handleSuccessfullResult() {
        toast(R.string.post_created_success)
        finish()
    }

    private fun handleFailedResult() {
        toast(R.string.error_occured)
    }
}
