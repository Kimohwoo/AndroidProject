package com.android.andriodproject.retrofit2

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: Application() {

    var networkService: NetworkService

    val retrofit: Retrofit
        get() = Retrofit.Builder().baseUrl("http://localhost:8083/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    init {
        networkService = retrofit.create(NetworkService::class.java)
    }

}