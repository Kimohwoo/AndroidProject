package com.android.andriodproject

import Converter.TO_GRID
import WeatherModel
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.AirListModel.AirListModel
import com.android.andriodproject.AirListModel.AirPollutionModel
import com.android.andriodproject.WeatherModel.WeatherListModel
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
        val toXY = Converter.convertGRID_GPS(TO_GRID, 37.55189, 126.9917933)
        Log.d("lsy", "x = " + toXY.x + ",y = " + toXY.y)

        //공공데이터 가져오기
        val time = getTime("HH00")
        var airResult = 0
        var fcstResult = 0
        var weatherResult = 0

        //AirPollutionService
        val airPollutionService = (applicationContext as MyApplication).airPollutionService
        val airListCall = airPollutionService.getAirPollution(serviceKey, 1, 100, "부산", resultType, 1.0)
        Log.d("lsy", "Air Url: " + airListCall.request().url().toString())
        airListCall.enqueue(object : Callback<AirListModel>{
            override fun onResponse(call: Call<AirListModel>, response: Response<AirListModel>) {
                val airList = response.body()
                val item = airList?.response?.body?.items
                val time = getTime("yyyy-MM-dd hh:00")
                Log.d("lsy", "시간 체크용 : ${time}")
                Log.d("lsy", "airList data item값: ${item}")
                Log.d("lsy", "airList data item값: ${item?.size}")
                binding.recyclerView.adapter = AirAdapter(this@WeatherActivity, item as List<AirPollutionModel>)

                item?.forEach { data ->
                    Log.d("lsy", "Grade값 찍히는지 : ${data.khaiGrade}")
                    if (data.khaiGrade == "1") {
                        airResult = 50
                        return
                    } else if (data.khaiGrade == "2") {
                        airResult = 40
                        return
                    } else if (data.khaiGrade == "3") {
                        airResult = 0
                        return
                    } else if (data.khaiGrade == "4") {
                        airResult = -10
                        return
                    } else {
                        airResult = 0
                        return
                    }
                }
                Log.d("lsy", "Air result 값 확인 : ${airResult}")
            }
            override fun onFailure(call: Call<AirListModel>, t: Throwable) {
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

        // getfcstInfoList2Call
        val fcstInfoList2Call = weatherService.getVilageFcstInfo(serviceKey, 1, 1000, resultType, apiDay, "0500", nx, ny)
        Log.d("lsy", "fcstInfoList2Call Url: " + fcstInfoList2Call.request().url().toString())
        fcstInfoList2Call.enqueue(object: Callback<WeatherListModel>{
            override fun onResponse(
                call: Call<WeatherListModel>,
                response: Response<WeatherListModel>
            ) {
                val weatherList = response.body()
                val item = weatherList?.response?.body?.items?.item
                Log.d("lsy", "weatherList data값: ${item}")
                binding.recyclerView.adapter =
                    MyAdapter(this@WeatherActivity, item as List<WeatherModel>)
                item?.forEach { data ->
                    Log.d("lsy", "category값 : ${data.category}")
                    if (data.category == "POP") {
                        rainProbability = data.fcstValue
                        Log.d("lsy", "rainProbability값 : ${rainProbability}")
                        if (rainProbability <= "10") {
                            fcstResult = 20
                            return
                        } else if (rainProbability > "10" && rainProbability <= "50"){
                            fcstResult = 10
                            return
                        } else if (rainProbability > "50") {
                            fcstResult = 0
                            return
                        }
                    }
                }
                Log.d("lsy", "fcstInfoList2Call Result: ${fcstResult}")
            }
            override fun onFailure(call: Call<WeatherListModel>, t: Throwable) {
                call.cancel()
            }
        })

        //getWeather
        val weatherListCall = weatherService.getWeather(serviceKey, 1,1000, resultType, apiDay, apiTime, nx, ny)
        Log.d("lsy", "Weather url: " + weatherListCall.request().url().toString())
        weatherListCall.enqueue(object: Callback<WeatherListModel> {
            override fun onResponse(call: Call<WeatherListModel>, response: Response<WeatherListModel>) {
                val weatherList = response.body()
                val item = weatherList?.response?.body?.items?.item
                Log.d("lsy", "weatherList data값: ${item}")
                binding.recyclerView.adapter = MyAdapter(this@WeatherActivity,item as List<WeatherModel>)

                //날씨 점수
                item?.forEach { data ->
                    if (data.category === "SKY") {
                        sky = data.fcstValue
                        if(sky == "1"){
                            weatherResult = 30
                            return
                        } else if(sky == "3"){
                            weatherResult = 20
                            return
                        } else if(sky == "4"){
                            weatherResult = 10
                            return
                        }
                    } else if (data.category === "PTY"){
                        rainPreTypes = data.fcstValue
                    } else if (data.category === "POP"){
                        rainProbability = data.fcstValue
                    }
                }
                Log.d("lsy", "air: ${airResult}, FCST: ${fcstResult}, weather: ${weatherResult}")
                binding.score.text = "Today: ${airResult + fcstResult + weatherResult}"
            }
            override fun onFailure(call: Call<WeatherListModel>, t: Throwable) {
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