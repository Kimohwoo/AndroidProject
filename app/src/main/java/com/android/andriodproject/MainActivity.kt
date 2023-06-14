package com.android.andriodproject

import android.location.LocationManager
import android.os.Bundle
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity
=======
>>>>>>> master
import com.android.andriodproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
<<<<<<< HEAD
    private val locationManager: LocationManager? = null
    private val REQUEST_CODE_LOCATION = 2

    //    var id: String = ""
//    var password: String = ""
=======
>>>>>>> master
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
<<<<<<< HEAD

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

//    fun Login(user: User){
//        val networkService = (applicationContext as MyApplication).networkService
//        val call = networkService.getLoginResponse(user)
//        call.enqueue(object: Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                TODO("Not yet implemented")
//                if(response.isSuccessful){
//                    Log.d("lsy", response.body().toString())
//                } else{
//                    Log.d("lsy", "FAILURE")
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                TODO("Not yet implemented")
//                Log.d("lsy", "Connection failure : ${t.localizedMessage}")
//            }
//
//        })
//
//
//    }


=======



    }
>>>>>>> master

}