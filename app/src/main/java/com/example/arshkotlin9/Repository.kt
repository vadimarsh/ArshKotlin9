package com.example.arshkotlin9

import android.graphics.Bitmap
import com.example.arshkotlin9.api.*
import com.example.arshkotlin9.dto.AttachmentModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream

class Repository(private val api: API) {

//    private val retrofit: Retrofit by lazy {
//        val client = OkHttpClient.Builder()
//            .addInterceptor(App.authTokenInterceptor)
//            .build()
//        Retrofit.Builder()
//            .baseUrl(SERVER_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }


    suspend fun authenticate(login: String, password: String): Response<Token> {
        val token: Response<Token> =
            api.authenticate(AuthRequestParams(username = login, password = password))
        return token
    }

    suspend fun register(login: String, password: String) =
        api.register(
            RegistrationRequestParams(
                login,
                password
            )
        )

    suspend fun getPosts() =
        api.getAllPosts()

    suspend fun getPostsRecent() =
        api.getRecent()

    suspend fun getPostsAfter(id: Long) =
        api.after(id)

    suspend fun getPostsBefore(id: Long) =
        api.before(id)

    suspend fun likePost(id: Long) =
        api.likePost(id)

    suspend fun dislikePost(id: Long) =
        api.dislikePost(id)

    suspend fun addNewPost(content: String, attid: String?) =
        api.createPost(PostRequest(content = content, attachmentId = attid))

    suspend fun addNewPost(content: String) = api.createPost(PostRequest(content = content))

    suspend fun sharePost(id: Long, content: String) =
        api.sharePost(id, PostRequest(sourceId = id, content = content))

    suspend fun upload(bitmap: Bitmap): Response<AttachmentModel> {
        // Создаем поток байтов
        val bos = ByteArrayOutputStream()
        // Помещаем Bitmap в качестве JPEG в этот поток
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val reqFIle =
            // Создаем тип медиа и передаем массив байтов с потока
            RequestBody.create(MediaType.parse("image/jpeg"), bos.toByteArray())
        val body =
        // Создаем multipart объект, где указываем поле, в котором
            // содержатся посылаемые данные, имя файла и медиафайл
            MultipartBody.Part.createFormData("file", "image.jpg", reqFIle)
        return api.uploadImage(body)
    }
}
