package com.android.andriodproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.andriodproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private var clicked = false
    private val CAMERA_PERMISSION_REQUEST_CODE = 1001
    private val CAMERA_USAGE_REQUEST_CODE = 1002
    private val CAMERA_STORAGE_REQUEST_CODE = 1003


    companion object {
        const val uid = "abcdefg"
        const val nickname = "Samkim"
        const val dogname = "똘똘이"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addBtn.setOnClickListener {
            onAddButtonClicked()
        }

        binding.cameraBtn.setOnClickListener {
            Toast.makeText(this, "플로팅1", Toast.LENGTH_SHORT).show()
            if(cameraPermission() && cameraStoragePermission()){
                openCamera()
            }else{
                requestPermissions()
            }
        }


        binding.galleryBtn.setOnClickListener {
            Toast.makeText(this, "플로팅2", Toast.LENGTH_SHORT).show()
        }
        binding.exerciseBtn.setOnClickListener {
            Toast.makeText(this, "플로팅3", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, GoogleMapsActivity::class.java)
            intent.putExtra(GoogleMapsActivity.uid, "$uid")
            startActivity(intent)
        }
    }

    //카메라 권한이 있는지 여부
    private fun cameraPermission() : Boolean{
        val cameraPermission = Manifest.permission.CAMERA
        val result = ContextCompat.checkSelfPermission(this, cameraPermission)
        return result == PERMISSION_GRANTED //<- 권한 결과를 반환하는데 true/flase를 반환
    }

    //카메라 저장할때 권한이 있는지 여부
    private fun cameraStoragePermission(): Boolean {
        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val result = ContextCompat.checkSelfPermission(this, storagePermission)
        return result == PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val cameraPermission = Manifest.permission.CAMERA
        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissionList = arrayOf(cameraPermission, storagePermission)

        ActivityCompat.requestPermissions(this, permissionList, CAMERA_PERMISSION_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 카메라 권한 허용
                if (hasStoragePermission()) {
                    openCamera()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_REQUEST_CODE
                    )
                }
            } else {
                // 카메라 권한 거부
                // 필요한 처리를 수행하세요.
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 저장소 권한 허용
                openCamera()
            } else {
                // 저장소 권한 거부
                // 필요한 처리를 수행하세요.
            }
        }
    }


    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.cameraBtn.visibility = View.GONE
            binding.galleryBtn.visibility = View.GONE
            binding.exerciseBtn.visibility = View.GONE
            binding.cameraBtn.isEnabled = true
            binding.galleryBtn.isEnabled = true
            binding.exerciseBtn.isEnabled = true
        } else {
            binding.cameraBtn.isEnabled = false
            binding.galleryBtn.isEnabled = false
            binding.exerciseBtn.isEnabled = false
            binding.cameraBtn.visibility = View.VISIBLE
            binding.galleryBtn.visibility = View.VISIBLE
            binding.exerciseBtn.visibility = View.VISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.cameraBtn.startAnimation(fromBottom)
            binding.galleryBtn.startAnimation(fromBottom)
            binding.exerciseBtn.startAnimation(fromBottom)
            binding.addBtn.startAnimation(rotateOpen)
        } else {
            binding.cameraBtn.startAnimation(toBottom)
            binding.galleryBtn.startAnimation(toBottom)
            binding.exerciseBtn.startAnimation(toBottom)
            binding.addBtn.startAnimation(rotateClose)
        }
    }
}
