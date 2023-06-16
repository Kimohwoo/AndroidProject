package com.android.andriodproject.retrofit2

import com.android.andriodproject.Model.MapData.ExerciseDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ExerciseData {
    @POST("mapData")
    fun postDataToServer(@Body request: ExerciseDTO): Call<ExerciseDTO>
}
