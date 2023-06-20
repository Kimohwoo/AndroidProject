package com.android.andriodproject.retrofit2


import android.util.Log
import com.android.andriodproject.Model.AirListModel.AirListModel
import com.android.andriodproject.Model.AirListModel.AirPollutionModel
import com.android.andriodproject.Model.UserModel
import com.android.andriodproject.Model.WeatherModel.WeatherListModel
import com.android.andriodproject.R
import com.android.andriodproject.getTime
import com.android.andriodproject.login.UserAccount
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserService {

    @POST("user-login")
    fun loginUser(
        @Body userModel: UserModel
    ): retrofit2.Call<UserModel>

    @POST("user-reg")
    fun regUser(
        @Body userModel: UserModel
    ): retrofit2.Call<UserModel>

    @PUT("user")
    fun updateUser(
        @Body userModel: UserModel
    ): retrofit2.Call<UserModel>

    @DELETE("user")
    fun deleteUser(
        @Body userModel: UserModel
    ): retrofit2.Call<Int>

}
