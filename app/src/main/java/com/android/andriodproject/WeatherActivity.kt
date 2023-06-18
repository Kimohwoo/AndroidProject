package com.android.andriodproject

import android.Manifest
import Converter.TO_GRID
import Converter.xyToSido
import WeatherModel
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
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
import kotlin.concurrent.thread

class WeatherActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherBinding
    private val PERMISSION_REQUEST_CODE = 1
    private lateinit var locationManager: LocationManager
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var airResult = 0
    private var skyResult = 0
    private var popResult = 0
    private var sky = 0
    private var pop = 0
    private var pty = 0
    private var pcp = 0f
    private var apiTime = ""
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val serviceKey = "cXgLGxZlC+V/06+8LDomc9m8TAR6VHymyLNbeFGuwGCIJcUfxAkVDHaPa3HQx5HeT0kWSkyFnh0JdmOV8rTiRg=="
        val resultType = "JSON"
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뷰 작업
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
//        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))



        //화면 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //GPS 위치, 경도 받아오기
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        requestLocationPermissions()
        startLocationUpdates()
        Log.d("lsy", "latitude: ${latitude}, longitude: ${longitude}")

        thread {
            Thread.sleep(4500)
            //위도 경도 -> x, y
            val toXY = Converter.convertGRID_GPS(TO_GRID, latitude, longitude)
            Log.d("lsy", "x = " + toXY.x + ",y = " + toXY.y)
            //공공데이터 가져오기
            val time = getTime("HH00")
            val apiDay = getTime("yyyyMMdd")
            val nx = toXY.x.toInt()
            val ny = toXY.y.toInt()
            name = xyToSido(nx, ny)
            when (time.toInt()) {
                in 501..759 -> apiTime = "0500"
                in 800..1059 -> apiTime = "0800"
                in 1100..1350 -> apiTime = "1100"
                in 1400..1659 -> apiTime = "1400"
                else -> apiTime = "0500"
            }

            //AirPollutionService
            val weatherService = (applicationContext as MyApplication).weatherService
            val weatherListCall =
                weatherService.getWeather(serviceKey, 1, 100, resultType, apiDay, apiTime, nx, ny)

            val airPollutionService = (applicationContext as MyApplication).airPollutionService
            val airListCall = airPollutionService.getAirPollution(
                serviceKey,
                1,
                100,
                name,
                resultType,
                1.0
            )

            Log.d("lsy", "xyToSido: ${xyToSido(nx, ny)}")
            Log.d("lsy", "Air Url: " + airListCall.request().url().toString())
            airListCall.enqueue(object : Callback<AirListModel> {
                override fun onResponse(
                    call: Call<AirListModel>,
                    response: Response<AirListModel>
                ) {
                    val airList = response.body()
                    val item = airList?.response?.body?.items
                    val time = getTime("yyyy-MM-dd hh:00")
                    val timePick = getTime("hhMM")

                    var item2 = arrayListOf<AirPollutionModel>()

                    if (item != null) {
                        item2 = item.filter {
                            it.khaiGrade != null
                        } as ArrayList<AirPollutionModel>
                    }
                    Log.d("lsy", "item의 사이즈 ${item?.size}")
                    Log.d("lsy", "item2의 사이즈 ${item2.size}")

                    binding.recyclerView.adapter =
                        AirAdapter(this@WeatherActivity, item2)

                    when (item2.get(0).khaiGrade) {
                        "1" -> airResult = 50
                        "2" -> airResult = 40
                        "3" -> airResult = 20
                        "4" -> -10
                        else -> 0
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
                            var item2 = arrayListOf<WeatherModel>()

                            if(item != null){
                                item2 = item.filter {
                                    data ->
                                    var category = data.category
                                    (category == "SKY" ||
                                    category == "POP" ||
                                    category == "TMP" ||
                                    category == "PTY" ||
                                    category == "PCP")&&
                                    ((data.fcstTime.toInt() in (apiTime.toInt() - 200)..(apiTime.toInt() + 200)))
                                }.distinctBy {
                                    it.category
                                } as ArrayList<WeatherModel>

                            }

                            Log.d("lsy", "사이즈 : ${item?.size}, weatherList data값: ${item}")
                            Log.d("lsy", "사이즈 : ${item2.size},weatherList data2값: ${item2}")
                            binding.recyclerView.adapter =
                                MyAdapter(this@WeatherActivity, item2)

                            //날씨 점수
                            weatherForeach(item2, apiTime, timePick)

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
        }
    }

    fun weatherForeach(item:  List<WeatherModel>, apiTime: String, timePick: String){
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
                } else if(data.category == "TMP"){
                    binding.temperature.text = "${name}  ${data.fcstValue}℃"
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
        binding.score.text = "산책점수 : ${airResult + skyResult + popResult}"

        //gif이미지
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
            if(timePick.toInt() > 600 || timePick.toInt() < 1800){
                Glide.with(this@WeatherActivity).load(R.raw.sunny).into(binding.weatherView)
            } else {
                Glide.with(this@WeatherActivity).load(R.raw.night).into(binding.weatherView)
            }
        } else if(sky >= 3){
            weatherImage
        }
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000000000, // Minimum time interval between location updates (in milliseconds)
                10f, // Minimum distance between location updates (in meters)
                locationListener
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude
            Log.d("lsy", "locationListener : ${latitude}, ${longitude}")
        }

//        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//
//        override fun onProviderEnabled(provider: String) {}
//
//        override fun onProviderDisabled(provider: String) {}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}