package com.android.andriodproject

import Converter.TO_GRID
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.Model.AirPollutionModel
import com.android.andriodproject.Model.PageListModel
import com.android.andriodproject.Model.WeatherModel
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
    lateinit var sky: String
    lateinit var rainPreTypes: String
    lateinit var rainProbability: String
    override fun onCreate(savedInstanceState: Bundle?) {
        val serviceKey = "cXgLGxZlC+V/06+8LDomc9m8TAR6VHymyLNbeFGuwGCIJcUfxAkVDHaPa3HQx5HeT0kWSkyFnh0JdmOV8rTiRg=="
        val resultType = "JSON"
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //GPS 위치, 경도 받아오기


        //위도 경도 -> x, y
        val toXY = Converter.convertGRID_GPS(TO_GRID, 37.579871128849334, 126.98935225645432)
        Log.d("lsy", "x = " + toXY.x + ",y = " + toXY.y)

        //공공데이터 가져오기
        val time = getTime("HH00")
        var result = 0
        //AirPollutionService
        val airPollutionService = (applicationContext as MyApplication).airPollutionService
        val airListCall = airPollutionService.getAirPollution(serviceKey, 1, 100, "부산", resultType, 1.0)
        Log.d("lsy", "Air Url: " + airListCall.request().url().toString())
        airListCall.enqueue(object : Callback<PageListModel>{
            override fun onResponse(call: Call<PageListModel>, response: Response<PageListModel>) {
                val airList = response.body()
                Log.d("lsy", "airList data값: ${airList?.response?.body?.items?.item?.size}")
                val item = airList?.response?.body?.items?.item
                binding.recyclerView.adapter = AirAdapter(this@WeatherActivity, item as List<AirPollutionModel>)

                item?.forEach { data ->
                    if (data.khaiGrade === "1") {
                        result = 50
                    } else if (data.khaiGrade === "2") {
                        result = 40
                    } else if (data.khaiGrade === "3") {
                        result = 20
                    } else {
                        result = 10
                    }
                }
                Log.d("lsy", "result 값 확인 : ${result}")
            }
            override fun onFailure(call: Call<PageListModel>, t: Throwable) {
                call.cancel()
            }

        })

        //weatherService
        val apiDay = getTime("yyyyMMdd")
        val apiTime = getTime("HHmm")
        val nx = toXY.x.toInt()
        val ny = toXY.y.toInt()
        Log.d("lsy", "api 시간 확인 Day: ${apiDay}, Time: ${apiTime}")

        val weatherService = (applicationContext as MyApplication).weatherService
        val weatherListCall = weatherService.getWeather(serviceKey, 1,1000, resultType, apiDay, apiTime, nx, ny)
        Log.d("lsy", "url: " + weatherListCall.request().url().toString())
        weatherListCall.enqueue(object: Callback<PageListModel> {
            override fun onResponse(call: Call<PageListModel>, response: Response<PageListModel>) {
                val weatherList = response.body()
                Log.d("lsy", "weatherList data값: ${weatherList?.response?.body?.items?.item?.size}")
                val item = weatherList?.response?.body?.items?.item

                binding.recyclerView.adapter = MyAdapter(this@WeatherActivity,item as List<WeatherModel>)

                //날씨 점수

                item?.forEach { data ->
                    if ((data.category == "SKY") && (data.fcstTime == time)) {
                        sky = data.fcstValue
                        return
                    } else if ((data.category == "PTY") && (data.fcstTime == time)) {
                        rainPreTypes = data.fcstValue
                    } else if ((data.category == "POP") && (data.fcstTime == time)) {
                        rainProbability = data.fcstValue
                    }
                    binding.score.text = "Today: ${result}"
                }
            }
            override fun onFailure(call: Call<PageListModel>, t: Throwable) {
                call.cancel()
            }

        })

        //시간, 날짜
        val getDay = getTime("yyyy년MM월dd일")
        val getTime = getTime("hh시MM분ss초")

        Log.d("lsy", "가져온 Day: ${getDay} 가져온 시간: ${getTime}")
        binding.dateView.text = getDay
        binding.timeView.text = getTime

        //gif 이미지 파일 넣기
        Glide.with(this).load(R.raw.sunny).into(binding.weatherView)
    }

}