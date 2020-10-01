package com.example.arshkotlin9

import com.example.arshkotlin9.api.API
import com.example.arshkotlin9.api.AuthRequestParams
import com.example.arshkotlin9.api.RegistrationRequestParams
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Repository {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://arshposts.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val api: API by lazy {
        retrofit.create(API::class.java)
    }

    suspend fun authenticate(login: String, password: String) = api.authenticate(
        AuthRequestParams(login, password)
    )

    suspend fun register(login: String, password: String) =
        api.register(
            RegistrationRequestParams(
                login,
                password
            )
        )
}
