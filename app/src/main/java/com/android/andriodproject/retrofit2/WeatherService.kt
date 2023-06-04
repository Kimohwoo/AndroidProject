package com.android.andriodproject.retrofit2

import com.android.andriodproject.Model.PageListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("getUltraSrtFcst")
    fun getWeather(
        @Query("serviceKey") serviceKey: String?,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("dataType") dataType: String,
        @Query("base_date") baseData: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): retrofit2.Call<PageListModel>

}