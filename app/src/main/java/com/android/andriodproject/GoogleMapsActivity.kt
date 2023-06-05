package com.android.andriodproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.android.andriodproject.databinding.ActivityGoogleMapsBinding

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // (자동 생성된 코드) SupportMapFragment 를 가져와서 지도가 준비되면 알림을 받습니다.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /** (자동 생성된 코드)
     * 사용가능한 맵을 조작함. (맵이 준비되면 이 콜백이 실행됨)
     * 이것으로 마커나 선, 리스터를 추가하거나 표시되는 지역 변경 가능 (디폴트로 호주 시드니를 표시됨)
     * Google Play 서비스가 설치되어 있지 않으면
     * SupportMapFragment 안에 서비스를 설치하라는 안내가 표시됨
     * 이 메서드는 Google Play 서비스를 설치하고 이 앱으로 돌아왔을 때만 실행됨
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}