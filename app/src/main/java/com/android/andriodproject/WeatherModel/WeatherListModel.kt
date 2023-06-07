package com.android.andriodproject.WeatherModel

import WeatherModel
import com.google.gson.annotations.SerializedName

//weather
data class WeatherListModel (
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
    var item: List<WeatherModel>
)





