package com.android.andriodproject.retrofit2

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: Application() {

    var weatherService: WeatherService
    var airPollutionService: AirPollutionService
    val weatherRetrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val airRetrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    init {
        weatherService = weatherRetrofit.create(WeatherService::class.java)
        airPollutionService = airRetrofit.create(AirPollutionService::class.java)
    }

}