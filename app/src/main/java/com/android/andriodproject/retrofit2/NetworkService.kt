package com.android.andriodproject.retrofit2

import com.android.andriodproject.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkService {

    @POST("android")
    fun getLoginResponse(@Body user: User): Call<String>

}