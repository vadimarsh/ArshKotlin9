package com.example.arshkotlin9

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.arshkotlin9.dto.AttachmentModel
import com.example.arshkotlin9.dto.PostModel
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.coroutines.launch
import retrofit2.Response
import splitties.toast.toast
import java.io.IOException

class NewPostActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    private var idSource: Long = -1L
    val REQUEST_IMAGE_CAPTURE = 1
    private var attachmentModel: AttachmentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        if (intent.extras != null) {
            idSource = intent.extras!!.getLong("id")
            attachPhotoImg.visibility = View.GONE
        } else {
            attachPhotoImg.visibility = View.VISIBLE
        }

        attachPhotoImg.setOnClickListener {
            dispatchTakePictureIntent()
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
                        if (attachmentModel != null) {
                            result = App.repository.addNewPost(
                                contentEdt.text.toString(),
                                attachmentModel?.id
                            )
                            attachmentModel == null
                        } else {
                            result = App.repository.addNewPost(
                                contentEdt.text.toString()
                            )
                        }
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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                lifecycleScope.launch {

                    dialog = createProgressDialog()
                    val imageUploadResult = App.repository.upload(it)
                    dialog?.dismiss()
                    if (imageUploadResult.isSuccessful) {
                        imageUploaded()
                        attachmentModel = imageUploadResult.body()
                    } else {
                        toast("Can't upload image")
                    }
                }
            }
        }
    }

    private fun imageUploaded() {
        transparetAllIcons()
        // Показываем красную галочку над фото
        //attachPhotoDoneImg.visibility = View.VISIBLE
    }

    private fun transparetAllIcons() {
        attachPhotoImg.setImageResource(R.drawable.ic_photo)
    }

    private fun createProgressDialog(): ProgressDialog? {
        return ProgressDialog(this@NewPostActivity).apply {
            setMessage(this@NewPostActivity.getString(R.string.please_wait))
            setTitle(R.string.create_new_post)
            setCancelable(false)
            setProgressBarIndeterminate(true)
            show()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
}
