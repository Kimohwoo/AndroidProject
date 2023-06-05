package com.android.andriodproject

import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.Model.PageListModel
import com.android.andriodproject.databinding.ActivityWeatherBinding
import com.android.andriodproject.retrofit2.MyAdapter
import com.android.andriodproject.retrofit2.MyApplication
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date


class WeatherActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherBinding
    lateinit var now: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        val serviceKey = "cXgLGxZlC+V/06+8LDomc9m8TAR6VHymyLNbeFGuwGCIJcUfxAkVDHaPa3HQx5HeT0kWSkyFnh0JdmOV8rTiRg=="
        val resultType = "JSON"
        now = Date(System.currentTimeMillis())
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //공공데이터 가져오기
        val apiFormatDay = SimpleDateFormat("yyyyMMdd")
        val apiFormatTime = SimpleDateFormat("HHmm")
        val apiDay = apiFormatDay.format(now)
        val apiTime = apiFormatTime.format(now)
        Log.d("lsy", "api 시간 확인 Day: ${apiDay}, Time: ${apiTime}")

        val weatherService = (applicationContext as MyApplication).weatherService
        val weatherListCall = weatherService.getWeather(serviceKey, 1,1000, resultType, apiDay, apiTime, 55, 127)
        Log.d("lsy", "url: " + weatherListCall.request().url().toString())
        weatherListCall.enqueue(object: Callback<PageListModel> {
            override fun onResponse(call: Call<PageListModel>, response: Response<PageListModel>) {
                val weatherList = response.body()
                Log.d("lsy", "weatherList data값: ${weatherList?.response?.body?.items?.item?.size}")
                val item = weatherList?.response?.body?.items?.item

                binding.recyclerView.adapter = MyAdapter(this@WeatherActivity, item)

                //날씨 점수
                val result = if(false){

                } else if(false){

                } else {
                    50
                }

                binding.score.text = "Today: ${result}"

            }

            override fun onFailure(call: Call<PageListModel>, t: Throwable) {
                call.cancel()
            }

        })

        //시간, 날짜
        val simpleDateFormatDay = SimpleDateFormat("yyyy년MM월dd일")
        val simpleDateFormatTime = SimpleDateFormat("HH시mm분ss초")
        val getDay = simpleDateFormatDay.format(now)
        val getTime = simpleDateFormatTime.format(now)

        Log.d("lsy", "가져온 Day: ${getDay} 가져온 시간: ${getTime}")
        binding.dateView.text = getDay
        binding.timeView.text = getTime

        //gif 이미지 파일 넣기
        Glide.with(this).load(R.raw.sunny).into(binding.weatherView)


    }
}