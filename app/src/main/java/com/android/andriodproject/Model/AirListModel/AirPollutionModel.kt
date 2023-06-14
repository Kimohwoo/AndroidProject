package com.android.andriodproject.Model.AirListModel
import com.google.gson.annotations.SerializedName

data class AirPollutionModel(
    @SerializedName("sidoName")
    var sidoName: String,
    @SerializedName("dataTime")
    var dataTime: String,
    @SerializedName("khaiGrade")
    var khaiGrade: String,
    @SerializedName("stationName")
    var stationName: String,
)