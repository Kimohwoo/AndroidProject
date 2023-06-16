package com.android.andriodproject

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.Model.MapData.MapDataRequest
import com.android.andriodproject.retrofit2.MapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat


class ResultActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val recommendDistance = 3500f
    private val email = "omega3060@naver.com"
    private val calorie = "30"
    private val daynum = SimpleDateFormat("yyMMdd").toString()



    companion object{
        const val FILE_NAME = "filename"
        const val TOTAL_DISTANCE = "totalDistance"
        const val EXERCISE_TIME = "exerciseTime"
        const val FILE_PATH = "filePath"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        displayPathFromTextFile()
    }

    private fun displayPathFromTextFile() {
//        val filename = "2306120643.txt"
//        val file = File(filesDir, filename)


        val intent = intent
        val fileName = intent.getStringExtra(FILE_NAME)
        val totalDistance = intent.getStringExtra(TOTAL_DISTANCE)?.toDoubleOrNull()?.toInt()
        val filePath = intent.getStringExtra(FILE_PATH)
        //운동시간 mills->분초로 변환해서 보내기
        val exerciseTime = intent.getLongExtra(EXERCISE_TIME,0) // exerciseTime을 Long 타입으로 받습니다.
        Log.d("KSJ", "운동시간 : $exerciseTime")
        val exerciseTimeSeconds = exerciseTime / 1000 // 밀리초를 초로 변환합니다.
        val minutes = exerciseTimeSeconds / 60 // 초를 분으로 변환합니다.
        val seconds = exerciseTimeSeconds % 60 // 남은 초를 계산합니다.

        val formattedExerciseTime = String.format("%02d:%02d", minutes, seconds)
        intent.putExtra(EXERCISE_TIME, formattedExerciseTime)
        setIntent(intent) // 수정된 intent를 현재 Intent에 설정합니다.

        Log.d("ksj12", "총거리 : $totalDistance m, 파일이름 : $fileName, 파일경로 : $filePath, 운동시간 : $formattedExerciseTime")

        val file = if (!fileName.isNullOrEmpty()) {
            File(filesDir, fileName)
        } else {
            Toast.makeText(this, "정상적으로 실행되지 않아 파일이 생성되지 않았습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (file.exists()) {
            val lines = file.readLines()
            val polylineOptions = PolylineOptions()
                .color(Color.RED) // 폴리라인의 색상 설정

            val latLngList = mutableListOf<LatLng>() // 폴리라인 좌표 리스트

            for (line in lines) {
                val parts = line.split(",")
                if (parts.size == 2) {
                    val latitude = parts[0].trim().substringAfter(":").trim().toDouble()
                    val longitude = parts[1].trim().substringAfter(":").trim().toDouble()

                    val latLng = LatLng(latitude, longitude)
                    latLngList.add(latLng) // 폴리라인 좌표를 리스트에 추가
                }
            }

            if (lines.isNotEmpty()) {
                // 이동 경로의 첫 번째 위치로 카메라 이동
                val firstLatLng = LatLng(lines.first().split(",")[0].trim().substringAfter(":").trim().toDouble(), lines.first().split(",")[1].trim().substringAfter(":").trim().toDouble())
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 15f))

                // 마지막 위치에 마커 추가
//                lines.last()를 사용하여 lines 리스트의 마지막 요소를 가져옴
//                split(",")을 사용하여 해당 텍스트 라인을 쉼표로 분할, 경도와 위도로 구성된 두 개의 부분 문자열이 생성.
//                trim()을 사용하여 각 부분 문자열의 앞뒤 공백을 제거.
//                substringAfter(":")을 사용하여 각 부분 문자열에서 콜론(:) 이후의 부분을 추출. 이렇게 하면 경도와 위도의 값을 얻음.
//                toDouble()을 사용하여 경도와 위도의 문자열 값을 실수형(Double)으로 변환.
//                변환된 경도와 위도 값을 사용하여 LatLng 객체를 생성.
//                MarkerOptions().position(lastLatLng)를 사용하여 마커를 생성하고, 이를 mMap의 addMarker() 메서드로 추가합니다. 이렇게 하면 마지막 위치에 해당하는 좌표에 마커가 표시됩니다.
                val lastLatLng = LatLng(lines.last().split(",")[0].trim().substringAfter(":").trim().toDouble(), lines.last().split(",")[1].trim().substringAfter(":").trim().toDouble())
                mMap.addMarker(MarkerOptions().position(lastLatLng))
            }

            // 폴리라인을 그립니다.
            polylineOptions.addAll(latLngList)
            mMap.addPolyline(polylineOptions)

            val retrofit = Retrofit.Builder()
                .baseUrl("http://localhost:8088/AndroidServer/") // 스프링 서버의 URL로 변경해야 합니다.
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(MapData::class.java)

            val request = MapDataRequest(
                email = email,
                fileName = fileName,
                filePath = filePath,
                exerciseTime = formattedExerciseTime,
                totalDistance = totalDistance.toString(),
                calorie = calorie,
                daynum = daynum
            )

            val call = apiService.postDataToServer(request)
            call.enqueue(object : Callback<MapDataRequest> {
                override fun onResponse(call: Call<MapDataRequest>, response: Response<MapDataRequest>) {
                    if (response.isSuccessful) {
                        // 서버 응답이 성공적으로 수신되었을 때의 처리
                        val responseData = response.body() // 응답 데이터
                        // ...
                    } else {
                        // 서버 응답이 실패했을 때의 처리
                        val errorCode = response.code() // 에러 코드
                        // ...
                    }
                }

                override fun onFailure(call: Call<MapDataRequest>, t: Throwable) {
                    // 네트워크 요청이 실패했을 때의 처리
                    // ...
                }
            })

        }
    }



    //
}
