package com.android.andriodproject.Model.MapData

import com.google.gson.annotations.SerializedName

data class MapDataRequest (
    @SerializedName("email") //<-스프링으로 넘겨주는 키값?
    var email : String,
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
    @SerializedName("daynum")
    var daynum : String,
)