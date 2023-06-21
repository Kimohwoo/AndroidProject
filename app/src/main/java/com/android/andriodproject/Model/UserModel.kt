package com.android.andriodproject.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class UserModel(
    @SerializedName("uid")
    var uId:String,
    @SerializedName("nickName")
    var nickName:String,
    @SerializedName("dogName")
    var dogName: String,
    @SerializedName("dogProfile")
    var dogProfile: String,
):Serializable