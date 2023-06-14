package com.android.andriodproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback, OnRequestPermissionsResultCallback {

    //권한이 거부되었는지 여부 추적
    private var permissionDenied = false
    //구글 맵을 mMap객체로 저장
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding
    //위치 업데이트 서비스를 제공하는 클라이언트
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    //위치 업데이트를 위한 요청
    private lateinit var locationRequest: LocationRequest
    //위치 업데이트 결과를 처리하는 콜백
    private lateinit var locationCallback: LocationCallback
    //지도에 실선 표기를 위한 객체
    private var polyline: Polyline? = null
    // 마지막으로 업데이트된 위치 좌표
    private var lastLocation: Location? = null
    //지도에 마지막으로 이동한 마커를 표시하기 위해 넣음
    private var marker: Marker? = null
    //이동경로 구하기 위해 넣음
    private var totalDistance: Double = 0.0
    // 파일 이름을 저장할 변수
    private var fileName: String = ""
    //(시작/일시정지/정지)버튼
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    // 이동 추적 상태(디폴트값) - 해당부분이 실행되었을때 자동으로 시작되지 않게 하기위해
    private var isTracking = false
    // 운동 시작 시간을 저장할 변수
    private var startTime: Long = 0
    // 운동 시간을 저장할 변수
    private var exerciseTime: Long = 0
    // 운동 타이머 작동 여부를 저장할 변수
    private var isTimerRunning = false
    // 운동 시간 표시 TextView
    private lateinit var exerciseTimeTextView: TextView
    // 운동 시간 업데이트를 위한 Handler
    private val exerciseHandler = Handler()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)  // 화면 꺼지지 않게.
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT    // 세로 모드 고정.

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // (자동 생성된 코드) SupportMapFragment를 호출(비동기적)하여 지도가 준비되면 알림을 받습니다.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 5000 // 5초마다 위치 업데이트
            fastestInterval = 5000 // 최소 5초 간격으로 업데이트(고정)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY //(GPS우선으로)
        }

        //위치 업데이트 결과를 처리하는 loacionCallback정의
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { onLocationUpdated(it) }
            }
        }

        // 버튼 초기화
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        stopButton = findViewById(R.id.stopButton)

        // 운동 시간 표시 TextView 초기화
        exerciseTimeTextView = findViewById(R.id.exerciseTimeTextView)


        startButton.setOnClickListener {
            startTracking()
            // 파일 이름 생성
            generateFileName()

        }

        pauseButton.setOnClickListener {
            pauseTracking()
        }

        stopButton.setOnClickListener {
            stopTracking()

        }

    }

    private fun startTracking() {
        if (!isTracking) {
            // 이동 추적 시작
            isTracking = true
            startButton.isEnabled = false
            pauseButton.isEnabled = true
            stopButton.isEnabled = true

            // 위치 업데이트 시작
            startLocationUpdates()

            // 운동 시작 시간 기록
            startTime = System.currentTimeMillis()

            // 운동 타이머 시작
            startExerciseTimer()
        }
    }

    private fun pauseTracking() {
        if (isTracking) {
            // 이동 추적 일시정지
            isTracking = false
            startButton.isEnabled = true
            pauseButton.isEnabled = false
            stopButton.isEnabled = true

            // 위치 업데이트 중지
            stopLocationUpdates()

            // 운동 타이머 정지
            stopExerciseTimer()
        }
    }

    private fun stopTracking() {
        // 이동 추적 정지
        isTracking = false
        startButton.isEnabled = true
        pauseButton.isEnabled = false
        stopButton.isEnabled = false

        // 위치 업데이트 중지
        stopLocationUpdates()

        // 운동 타이머 정지
        stopExerciseTimer()

        // 운동 시간 계산
        exerciseTime = System.currentTimeMillis() - startTime

        // 기타 필요한 정지 작업 수행
        // 예: 데이터 저장, 리셋 등

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.FILE_NAME, "$fileName") // 파일 이름을 전달
        intent.putExtra(ResultActivity.TOTAL_DISTANCE, "$totalDistance") // 누적 이동 거리를 전달
        intent.putExtra(ResultActivity.EXERCISE_TIME, "$exerciseTime") // 운동 시간을 전달
        startActivity(intent)
    }

    // 운동 타이머 시작
    private fun startExerciseTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            val exerciseHandler = Handler()
            exerciseHandler.postDelayed(object : Runnable {
                override fun run() {
                    if (isTimerRunning) {
                        val currentTime = System.currentTimeMillis()
                        val elapsedTime = currentTime - startTime
                        // 운동 시간 업데이트
                        exerciseTime = elapsedTime

                        // 운동 시간을 TextView에 표시
                        val formattedTime = formatExerciseTime(elapsedTime)
                        exerciseTimeTextView.text = "$formattedTime"
                        exerciseTimeTextView.gravity = Gravity.CENTER

                        // 1초마다 타이머 업데이트
                        exerciseHandler.postDelayed(this, 1000)
                    }
                }
            }, 1000) // 1초마다 실행하도록 설정
        }
    }

    private fun formatExerciseTime(timeInMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // 운동 타이머 정지
    private fun stopExerciseTimer() {
        isTimerRunning = false
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    /** (자동 생성된 코드)
     * 사용가능한 맵을 조작함. (맵이 준비되면 이 콜백이 실행됨)
     * 이것으로 마커나 선, 리스터를 추가하거나 표시되는 지역을 변경할 수 있음 (디폴트로 호주 시드니를 표시됨)
     * Google Play 서비스가 설치되어 있지 않으면 SupportMapFragment 안에 서비스를 설치하라는 안내가 표시됨
     * 이 메서드는 Google Play 서비스를 설치하고 이 앱으로 돌아왔을 때만 실행됨
     */
    //맵이 준비되면 호출되는 콜백
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //시작할때 현재위치 표시 활성화
        enableMyLocation()
        //시작할때 폴리라인 객체 초기화(노란색으로 움직인 거리를 표기할거란걸 알 수 있음)
        polyline = mMap.addPolyline(PolylineOptions().color(Color.YELLOW))


        // 시작할때 totalDistance 변수 초기화
        totalDistance = 0.0

    }

    @SuppressLint("MissingPermission")
    //현재위치 표시를 활성화
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true //현재위치 권한이 있는경우
            startLocationUpdates() //<-위치업데이트를 시작하는 메서드 호출
            return
        }

        //현재위치 표시권한X인경우, 이유 다이얼로그 표시
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            PermissionUtils.RationaleDialog.newInstance(LOCATION_PERMISSION_REQUEST_CODE, true).show(supportFragmentManager, "dialog")
            return
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }

    //위치 업데이트를 시작하는 역할
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

        //권한이 있는경우 fusedLocationProviderClient를 사용해서 위치업데이트 요청
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        // 혹시 안드로이드 스튜디오에서 비정상적으로 권한 요청 오류를 표시할 경우, 'Alt+Enter'로 표시되는 제안 중,
        // Suppress: Add @SuppressLint("MissingPermission") annotation
        // 을 클릭할 것
        // (에디터가 원래 권한 요청이 필요한 코드 주변에서만 권한 요청 코딩을 허용했었기 때문임.
        //  현재 우리 코딩처럼 이렇게 별도의 메소드에 권한 요청 코드를 작성하지 못하게 했었음)
    }


    //권한 요청결과를 처리하는 역할
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION) || isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            enableMyLocation() //<-요청코드가 LOCATION_PERMISSION_REQUEST_CODE와 일치하면 위치표시 활성화
        } else {
            permissionDenied = true //권한이 거부된 경우
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

    // 파일 이름 생성 함수
    private fun generateFileName() {
        val now = Date(System.currentTimeMillis())
        val formatTime = SimpleDateFormat("yyMMddhhmm")
        fileName = "${formatTime.format(now)}.txt"
    }

    //파일 내용 갱신 함수
    private fun writeToFile(latitude: Double, longitude: Double) {
        val file = File(filesDir, fileName)

        try {
            if (!file.exists()) {
                file.createNewFile()
            }

            val now = Date(System.currentTimeMillis())
            val fileContents = "Latitude: $latitude, Longitude: $longitude\n" // 추가할 내용
            FileWriter(file, true).use { writer ->
                writer.append(fileContents)
            }

            val filePath = file.absolutePath
            Log.d("KSJ", "File path: $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onLocationUpdated(location: Location) {
        val currentLatitude = location.latitude
        val currentLongitude = location.longitude

        val currentLatLng = LatLng(currentLatitude, currentLongitude)


        // 좌표를 랜덤하게 조금씩 이동시키는 코드
        // ☆(중요)새로운 위치를 생성할 때 랜덤한 값을 사용하여 현재 위치를 조금씩 이동시킨다는 점입니다. 이렇게 되면 이동 거리가 계속 0으로 나오는 문제가 발생.
//        val newLatitude = currentLatitude + (Math.random() - 0.5) * 0.01
//        val newLongitude = currentLongitude + (Math.random() - 0.5) * 0.01
//
//        val currentLatLng = LatLng(newLatitude, newLongitude)

            // 이전 마커 제거
            marker?.remove()
            // 새로운 위치에 마커 표시
            marker = mMap.addMarker(MarkerOptions().position(currentLatLng).title("현재위치"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))


            val previousLocation = lastLocation?.let { Location(it) }
            lastLocation = location

            if (previousLocation != null) {
                val distance = previousLocation.distanceTo(location).toDouble()
                totalDistance += distance
                Log.d("KSJ", "이동 거리: $distance 미터")
                Log.d("KSJ", "누적 이동 거리: $totalDistance 미터")
            }

        if(startButton.isEnabled === true){
            Log.d("KSJ", "아직 스타트 버튼을 안눌름")
        }
        if(startButton.isEnabled === false) {
            //Polyline에 좌표 추가
            val points = polyline?.points?.toMutableList() ?: mutableListOf()
            points.add(currentLatLng)
            polyline?.points = points


//        Log.d("KSJ", "위도: $newLatitude, 경도: $newLongitude")
            Log.d("KSJ", "위도: $currentLatitude, 경도: $currentLongitude")

            if (writeToFile(location.latitude, location.longitude) === null) {
                Log.d("KSJ", "시작버튼을 누르지 않았습니다.")
            } else {
                writeToFile(location.latitude, location.longitude)

            }
        }


    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
