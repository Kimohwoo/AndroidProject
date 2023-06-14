
import android.location.LocationManager
import android.util.Log
import com.android.andriodproject.R
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName

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

)