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

    //어떻게 하면 push 가 될까요?
    // 1. 코멘트를 추가했습니다.
    // 2. 오우 씨가 현재 파일에 변경 사항을 push 했습니다.
    // 3. 성호가 현재 파일을 git push를 했는데 2번 변경사항 때문에 reject되었습니다.

}