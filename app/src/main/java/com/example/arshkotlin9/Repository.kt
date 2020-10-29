package com.example.arshkotlin9

import com.example.arshkotlin9.api.*
import retrofit2.Response

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

    suspend fun addNewPost(content: String) = api.createPost(PostRequest(content = content))

    suspend fun sharePost(id: Long, content: String) =
        api.sharePost(id, PostRequest(sourceId = id, content = content))

}
