package com.android.andriodproject.Model

import java.util.Date

data class BoardModel(
    var no: Long,
    var title:String,
    var author:String,
    var content:String,
    var regDate: Date,
    var updateDay: Date,
    var imgId:Int,
    )