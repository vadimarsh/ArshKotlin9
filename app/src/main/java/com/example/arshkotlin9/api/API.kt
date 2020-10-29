package com.example.arshkotlin9.api

import com.example.arshkotlin9.dto.PostModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class AuthRequestParams(val username: String, val password: String)

data class Token(val token: String)

data class RegistrationRequestParams(val username: String, val password: String)

data class PostRequest(
    val id: Long = -1,
    val sourceId: Long? = null,
    val content: String? = null,
    val link: String? = null,
    val attachmentId: String? = null
)

interface API {
    @POST("api/v1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun register(@Body registrationRequestParams: RegistrationRequestParams): Response<Token>

    @GET("api/v1/posts/recent")
    suspend fun getRecent(): Response<List<PostModel>>

    @GET("api/v1/posts/after/{id}")
    suspend fun after(@Path("id") id: Long): Response<List<PostModel>>

    @GET("api/v1/posts/before/{id}")
    suspend fun before(@Path("id") id: Long): Response<List<PostModel>>

    @GET("api/v1/posts")
    suspend fun getAllPosts(): Response<List<PostModel>>

    @POST("api/v1/posts/like/{id}")
    suspend fun likePost(@Path("id") id: Long): Response<PostModel>

    @POST("api/v1/posts/dislike/{id}")
    suspend fun dislikePost(@Path("id") id: Long): Response<PostModel>

    @POST("api/v1/posts")
    suspend fun createPost(@Body createPostRequest: PostRequest): Response<PostModel>

    @POST("api/v1/posts/share/{id}")
    suspend fun sharePost(
        @Path("id") id: Long,
        @Body createPostRequest: PostRequest
    ): Response<PostModel>

}