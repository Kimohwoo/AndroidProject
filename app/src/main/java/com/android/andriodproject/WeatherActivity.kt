package com.android.andriodproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.andriodproject.databinding.ActivityWeatherBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date

class WeatherActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //시간, 날짜
        val now = Date(System.currentTimeMillis())
        val simpleDateFormatDay = SimpleDateFormat("yyyy년MM월dd일")
        val simpleDateFormatTime = SimpleDateFormat("HH시mm분ss초")
        val getDay = simpleDateFormatDay.format(now)
        val getTime = simpleDateFormatTime.format(now)

        Log.d("lsy", "가져온 Day: ${getDay} 가져온 시간: ${getTime}")
        binding.dateView.text = getDay + getTime
        
        //gif 이미지 파일 넣기
        val imageView = binding.nowImage
        Glide.with(this).load(R.raw.sun).into(binding.weatherView)


    }
}