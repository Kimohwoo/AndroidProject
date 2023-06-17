package com.android.andriodproject.Model

import com.google.gson.annotations.SerializedName

data class BoardListModel (
    @SerializedName("item")
    var item: List<BoardModel>
    )