package com.android.andriodproject.Model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class BoardModel(
    @SerializedName("no")
    var no: Long,
    @SerializedName("title")
    var title:String,
    @SerializedName("author")
    var author:String,
    @SerializedName("content")
    var content:String,
    @SerializedName("regdate")
    var regdate: Date,
    @SerializedName("updateDay")
    var updateDay: Date,
    @SerializedName("imgId")
    var imgId:Int,
    )
