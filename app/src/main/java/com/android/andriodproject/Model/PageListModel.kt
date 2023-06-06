package com.android.andriodproject.Model

import com.google.gson.annotations.SerializedName

data class PageListModel (
    var response: Response
)

data class Response(
    var body: Body
)

data class Body(
    var items: Items
)

data class Items(
    @SerializedName("item")
    var item: List<ItemModel>,
//    @SerializedName("item")
//    var item: List<AirPollutionModel>,
)





