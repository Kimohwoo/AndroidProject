package com.android.andriodproject.retrofit2


import com.android.andriodproject.Model.AirListModel.AirListModel
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