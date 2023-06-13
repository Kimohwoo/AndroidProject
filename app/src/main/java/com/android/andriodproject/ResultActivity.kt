package com.android.andriodproject

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils.split
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.File


class ResultActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    companion object{
        const val FILE_NAME = "filename"
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

        val defaultFileName = "2306120643.txt"
        val intent = intent
        val fileName = intent.getStringExtra(FILE_NAME)
        val file = if (!fileName.isNullOrEmpty()) {
            File(filesDir, fileName)
        } else {
            File(filesDir, defaultFileName)
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
        }
    }
}
