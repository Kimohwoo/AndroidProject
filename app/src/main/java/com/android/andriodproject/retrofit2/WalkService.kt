package com.android.andriodproject.retrofit2

import com.android.andriodproject.Model.WalkModel.WalkListModel
import com.android.andriodproject.Model.WalkModel.WalkModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WalkService {
    //baseurl : http://10.100.105.168:8083/
    @GET("api/exercise/findAll")
    fun searchAllData(
        @Query("uid") uid: String,
    ): Call<List<WalkModel>>

    @GET("api/exercise/findByDate")
    fun searchData(
        @Query("uid") uid: String,
        @Query("dayNum") dayNum: String,
    ): Call<List<WalkModel>>
}