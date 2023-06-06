package com.android.andriodproject.Model

import com.google.gson.annotations.SerializedName

open class ItemModel
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

    data class WeatherModel(

        @SerializedName("baseDate")
        var baseDate: String,
        @SerializedName("baseTime")
        var baseTime: String,
        @SerializedName("category")
        var category: String,
        @SerializedName("fcstDate")
        var fcstDate: String,
        @SerializedName("fcstTime")
        var fcstTime: String,
        @SerializedName("fcstValue")
        var fcstValue: String,
        @SerializedName("nx")
        var nx: Int,
        @SerializedName("ny")
        var ny: Int,

        )
