package com.android.andriodproject

import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.android.andriodproject.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    companion object {
        const val uid = "abcdefg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.exerciseStart.setOnClickListener {

            val intent = Intent(this, GoogleMapsActivity::class.java)
            intent.putExtra(GoogleMapsActivity.uid, "$uid") // 파일경로
            startActivity(intent)

        }

    }
}