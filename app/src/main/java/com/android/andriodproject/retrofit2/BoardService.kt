package com.android.andriodproject.retrofit2

import com.android.andriodproject.Model.BoardListModel
import com.android.andriodproject.Model.BoardModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BoardService {

    @GET("board/list")
    fun getBoardList(
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
    ): retrofit2.Call<BoardListModel>

    @POST("board/detail")
    fun postBoard(
        @Body
        board: BoardModel
    ): retrofit2.Call<String>

}