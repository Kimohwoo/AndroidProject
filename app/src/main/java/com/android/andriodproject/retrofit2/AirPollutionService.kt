package com.android.andriodproject.retrofit2


import android.util.Log
import com.android.andriodproject.Model.AirListModel.AirListModel
import com.android.andriodproject.Model.AirListModel.AirPollutionModel
import com.android.andriodproject.Model.WeatherModel.WeatherListModel
import com.android.andriodproject.R
import com.android.andriodproject.getTime
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirPollutionService {

    @GET("getCtprvnRltmMesureDnsty")
    fun getAirPollution(
        @Query("serviceKey") serviceKey: String?,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("sidoName") sidoName: String,
        @Query("returnType") returnType: String,
        @Query("ver") ver: Double
    ): retrofit2.Call<AirListModel>

}
