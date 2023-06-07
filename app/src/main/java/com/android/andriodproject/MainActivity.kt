package com.android.andriodproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.andriodproject.databinding.ActivityMainBinding
import com.android.andriodproject.retrofit2.MyApplication
import com.android.andriodproject.retrofit2.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    var id: String = ""
    var password: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {
            id = binding.id.text.toString()
            password = binding.password.text.toString()
//            val user = User()
//            user.id = id
//            user.password = password
//
//            Log.d("lsy", "id: ${user.id} pw: ${user.password}")
//            Login(user)
        }

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

//        })


//    }

}