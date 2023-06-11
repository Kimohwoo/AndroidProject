package com.android.andriodproject

import Converter.TO_GRID
import Converter.xyToSido
import WeatherModel
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.Model.AirListModel.AirListModel
import com.android.andriodproject.Model.AirListModel.AirPollutionModel
import com.android.andriodproject.Model.WeatherModel.WeatherListModel
import com.android.andriodproject.databinding.ActivityWeatherBinding
import com.android.andriodproject.retrofit2.AirAdapter
import com.android.andriodproject.retrofit2.MyAdapter
import com.android.andriodproject.retrofit2.MyApplication
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherBinding
    lateinit var gifImage: String
    override fun onCreate(savedInstanceState: Bundle?) {
        val serviceKey = "cXgLGxZlC+V/06+8LDomc9m8TAR6VHymyLNbeFGuwGCIJcUfxAkVDHaPa3HQx5HeT0kWSkyFnh0JdmOV8rTiRg=="
        val resultType = "JSON"
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //GPS 위치, 경도 받아오기

        //위도 경도 -> x, y
        val toXY = Converter.convertGRID_GPS(TO_GRID, 37.55189, 126.9917933)
        Log.d("lsy", "x = " + toXY.x + ",y = " + toXY.y)

        //공공데이터 가져오기
        val time = getTime("HH00")
        val apiDay = getTime("yyyyMMdd")
        val nx = toXY.x.toInt()
        val ny = toXY.y.toInt()
        var airResult = 0
        var skyResult = 0
        var popResult = 0
        var sky = 0
        var pop = 0
        var pty = 0
        var pcp = 0f
        var apiTime = ""

        when(time.toInt()){
            in 501..759 -> apiTime = "0500"
            in 800..1059 -> apiTime = "0800"
            in 1100..1350 -> apiTime = "1100"
            in 1400..1659 -> apiTime = "1400"
            else -> apiTime = "0500"
        }

        val weatherService = (applicationContext as MyApplication).weatherService

        val weatherListCall = weatherService.getWeather(serviceKey, 1,100, resultType, apiDay, apiTime, nx, ny)

        val airPollutionService = (applicationContext as MyApplication).airPollutionService
        val airListCall = airPollutionService.getAirPollution(serviceKey, 1, 100, xyToSido(nx, ny), resultType, 1.0)

        //AirPollutionService
        Log.d("lsy", "Air Url: " + airListCall.request().url().toString())
            airListCall.enqueue(object : Callback<AirListModel> {
                override fun onResponse(
                    call: Call<AirListModel>,
                    response: Response<AirListModel>
                ) {
                    val airList = response.body()
                    val item = airList?.response?.body?.items
                    val time = getTime("yyyy-MM-dd hh:00")

                    Log.d("lsy", "airList data item값: ${item?.size}")

                    binding.recyclerView.adapter =
                        AirAdapter(this@WeatherActivity, item as List<AirPollutionModel>)

                    item?.forEach { data ->
                        if (data.khaiGrade != null) {
                            when (data.khaiGrade) {
                                "1" -> airResult = 50
                                "2" -> airResult = 40
                                "3" -> airResult = 20
                                "4" -> -10
                                else -> 0
                            }
                            return@forEach
                        }
                    }
                    Log.d("lsy", "Air result 값 확인 : ${airResult}")

                    //weatherService
                    Log.d("lsy", "Weather url: " + weatherListCall.request().url().toString())
                    weatherListCall.enqueue(object : Callback<WeatherListModel> {
                        override fun onResponse(
                            call: Call<WeatherListModel>,
                            response: Response<WeatherListModel>
                        ) {
                            val weatherList = response.body()
                            val item = weatherList?.response?.body?.items?.item
                            Log.d("lsy", "weatherList data값: ${item}")
                            binding.recyclerView.adapter =
                                MyAdapter(this@WeatherActivity, item as List<WeatherModel>)

                            //날씨 점수
                            item?.forEach{ data ->
                                if ((data.fcstTime.toInt() == apiTime.toInt()) || (data.fcstTime.toInt() == apiTime.toInt() + 100)) {
                                    if (data.category == "SKY") {
                                        sky = data.fcstValue.toInt()
                                        Log.d("lsy", "weatherList sky 값 들어오는지 : ${sky}")
                                        when (sky) {
                                            1 -> skyResult = 30
                                            3 -> skyResult = 20
                                            4 -> skyResult = 10
                                            else -> skyResult = 0
                                        }
                                        return@forEach
                                    } else if(data.category == "POP"){
                                        pop = data.fcstValue.toInt()
                                        Log.d("lsy", "POP 값 : ${pop}")
                                        when(pop){
                                            in 0..10 -> popResult = 20
                                            in 11..30 -> popResult = 15
                                            in 31..60 -> popResult = 8
                                            in 61..99 -> popResult = 3
                                            else -> popResult = 0
                                        }
                                        return@forEach
                                    } else if(data.category == "TMN"){
                                       data.fcstValue
                                    } else if(data.category == "PTY"){
                                        pty = data.fcstValue.toInt()
                                    } else if(data.category == "PCP"){
                                        var forFloat = ""
                                        if(data.fcstValue.equals("강수없음")) {
                                            forFloat = "0mm"
                                        } else {
                                            forFloat = data.fcstValue
                                        }
                                        val floatValue = forFloat.substringBefore("mm").toFloatOrNull()
                                        if (floatValue != null) {
                                            pcp = floatValue
                                        } else {
                                            Log.d("lsy", "pcp 형변환 확인:${pcp}, floatValue: ${floatValue}")
                                        }
                                    }
                                }
                            }
                            Log.d("lsy", "air: ${airResult}, weather: ${skyResult + popResult}")
                            binding.score.text = "Today: ${airResult + skyResult + popResult}"
                            //gif 이미지
                            val weatherImage = if(pty == 1){
                                if(pcp < 30f) {
                                    when (pop) {
                                        in 50..100 -> Glide.with(this@WeatherActivity).load(R.raw.rain).into(binding.weatherView)
                                        else -> Glide.with(this@WeatherActivity).load(R.raw.cloud).into(binding.weatherView)
                                    }
                                } else if(pcp >= 30f){
                                    Glide.with(this@WeatherActivity).load(R.raw.heavyrain).into(binding.weatherView)
                                } else {
                                    Log.d("lsy", "pcp: ${pcp}, pop: ${pop}")
                                }
                            } else if(pty == 2 || pty ==3) {
                                Glide.with(this@WeatherActivity).load(R.raw.snow).into(binding.weatherView)
                            } else {
                                Glide.with(this@WeatherActivity).load(R.raw.cloud).into(binding.weatherView)
                            }
                            if(sky == 1){
                                if(time.toInt() > 600 || time.toInt() < 1800){
                                    Glide.with(this@WeatherActivity).load(R.raw.sunny).into(binding.weatherView)
                                } else {
                                    Glide.with(this@WeatherActivity).load(R.raw.night).into(binding.weatherView)
                                }
                            } else if(sky >= 3){
                                weatherImage
                            }
                        }
                        override fun onFailure(call: Call<WeatherListModel>, t: Throwable) {
                            call.cancel()
                        }
                    })
                }
                override fun onFailure(call: Call<AirListModel>, t: Throwable) {
                    call.cancel()
                }
            })

        //시간, 날짜
        val getDay = getTime("yyyy년MM월dd일")
        val getTime = getTime("hh시MM분ss초")

        Log.d("lsy", "가져온 Day: ${getDay} 가져온 시간: ${getTime}")
        binding.dateView.text = getDay
        binding.timeView.text = getTime

    }
}
