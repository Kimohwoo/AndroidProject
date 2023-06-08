package com.android.andriodproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat

import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.android.andriodproject.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.android.andriodproject.PermissionUtils.isPermissionGranted
import com.android.andriodproject.databinding.ActivityGoogleMapsBinding

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback, OnRequestPermissionsResultCallback {

    private var permissionDenied = false
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 5000 // 5초마다 위치 업데이트
            fastestInterval = 5000 // 최소 5초 간격으로 업데이트
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { onLocationUpdated(it) }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            startLocationUpdates()
            return
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            PermissionUtils.RationaleDialog.newInstance(LOCATION_PERMISSION_REQUEST_CODE, true).show(supportFragmentManager, "dialog")
            return
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: 부족한 권한을 요청하기 위해
            //여기서 ActivityCompat#requestPermissions를 호출하고,
            // 그 다음에는 권한이 부여될 경우를 처리하기 위해
            // public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)를 재정의하는 것을 고려해보세요.
            // 자세한 내용은 ActivityCompat#requestPermissions의 문서를 참조하세요.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION) || isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            enableMyLocation()
        } else {
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }

    private fun onLocationUpdated(location: Location) {
        //(현재위치 5초마다 업데이트)
//        val currentLatLng = LatLng(location.latitude, location.longitude)
//        mMap.clear() // 기존 마커 제거
//        mMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
//
//        // 콘솔에 위치 좌표 출력
//        val latitude = location.latitude
//        val longitude = location.longitude
//        Log.d("KSJ", "위도: $latitude, 경도: $longitude")

        val currentLatitude = location.latitude
        val currentLongitude = location.longitude

        // 좌표를 랜덤하게 조금씩 이동시키는 코드
        val newLatitude = currentLatitude + (Math.random() - 0.5) * 0.01
        val newLongitude = currentLongitude + (Math.random() - 0.5) * 0.01

        val currentLatLng = LatLng(newLatitude, newLongitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(currentLatLng).title("현재위치"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

        Log.d("KSJ", "위도: $newLatitude, 경도: $newLongitude")

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
