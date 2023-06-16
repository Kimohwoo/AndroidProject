package com.android.andriodproject.retrofit2

import com.android.andriodproject.Model.BoardModel
import retrofit2.http.GET
import retrofit2.http.Query

interface BoardService {
    @GET("board/list")
    fun getWeather(
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("dataType") dataType: String,
    ): retrofit2.Call<BoardModel>

}