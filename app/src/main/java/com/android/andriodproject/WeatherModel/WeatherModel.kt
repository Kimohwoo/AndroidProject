
import com.google.gson.annotations.SerializedName

data class WeatherModel(
    //WeatherModel
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