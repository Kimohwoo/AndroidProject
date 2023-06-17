package com.android.andriodproject.Model.MapData

import com.google.gson.annotations.SerializedName

data class ExerciseDTO (
    @SerializedName("uid") //<-스프링에서 받아올 때 키값?
    var uid : String, //<- 스프링으로 보낼 때 정의
    @SerializedName("fileName")
    var fileName : String,
    @SerializedName("filePath")
    var filePath : String?,
    @SerializedName("exerciseTime")
    var exerciseTime : String,
    @SerializedName("totalDistance")
    var totalDistance : String,
    @SerializedName("calorie")
    var calorie : String,
    @SerializedName("dayNum")
    var dayNum : String,
)