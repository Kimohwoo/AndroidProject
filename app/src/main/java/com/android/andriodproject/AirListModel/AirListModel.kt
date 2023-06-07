package com.android.andriodproject.AirListModel


import com.google.gson.annotations.SerializedName

data class AirListModel (
    var response: Response
)

data class Response(
    var body: Body
)

data class Body(
    @SerializedName("items")
    var items: List<AirPollutionModel>
)




