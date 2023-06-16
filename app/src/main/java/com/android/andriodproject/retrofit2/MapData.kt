package com.android.andriodproject.retrofit2

import com.android.andriodproject.Model.MapData.MapDataRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MapData {
    @POST("/mapData")
    fun postDataToServer(@Body request: MapDataRequest): Call<MapDataRequest>
}
