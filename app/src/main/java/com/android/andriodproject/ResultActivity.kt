package com.android.andriodproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File


class ResultActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(this)

        displayPathFromTextFile()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        displayPathFromTextFile()
    }

    private fun displayPathFromTextFile() {
        val filename = "location_history.txt"
        val file = File(filesDir, filename)

        if (file.exists()) {
            val lines = file.readLines()
            for (line in lines) {
                val parts = line.split(",")
                if (parts.size == 2) {
                    val latitude = parts[0].trim().substringAfter(":").trim().toDouble()
                    val longitude = parts[1].trim().substringAfter(":").trim().toDouble()

                    val latLng = LatLng(latitude, longitude)
                    mMap.addMarker(MarkerOptions().position(latLng))
                }
            }

            if (lines.isNotEmpty()) {
                // 이동 경로의 첫 번째 위치로 카메라 이동
                val firstLatLng = LatLng(lines.first().split(",")[0].trim().substringAfter(":").trim().toDouble(), lines.first().split(",")[1].trim().substringAfter(":").trim().toDouble())
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 18f))
            }
        }
    }
}
