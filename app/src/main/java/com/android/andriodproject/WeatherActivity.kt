package com.android.andriodproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.andriodproject.databinding.ActivityWeatherBinding
import com.google.gson.JsonArray
import com.google.gson.annotations.JsonAdapter
import okhttp3.Request
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}