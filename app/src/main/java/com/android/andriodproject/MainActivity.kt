package com.android.andriodproject

import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.android.andriodproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val locationManager: LocationManager? = null
    private val REQUEST_CODE_LOCATION = 2

    //    var id: String = ""
//    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btn.setOnClickListener {
//            id = binding.id.text.toString()
//            password = binding.password.text.toString()
//            val user = User()
//            user.id = id
//            user.password = password
//
//            Log.d("lsy", "id: ${user.id} pw: ${user.password}")
//            Login(user)
//        }

    }


}