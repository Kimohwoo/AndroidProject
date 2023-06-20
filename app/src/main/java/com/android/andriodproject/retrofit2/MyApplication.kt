package com.android.andriodproject.retrofit2

import android.app.Application
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: Application() {

    var weatherService: WeatherService
    var airPollutionService: AirPollutionService
    var walkService: WalkService
    val weatherRetrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    val airRetrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val walkRetrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://10.100.105.168:8083/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()



    init {
        weatherService = weatherRetrofit.create(WeatherService::class.java)
        airPollutionService = airRetrofit.create(AirPollutionService::class.java)
        walkService = walkRetrofit.create(WalkService::class.java)
    }

}