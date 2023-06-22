package com.android.andriodproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.widget.Toast
import com.android.andriodproject.Model.UserModel
import com.android.andriodproject.databinding.ActivityEditUserBinding
import com.android.andriodproject.retrofit2.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getSerializableExtra("user") as UserModel

        binding.nickName.setText(user.nickName)
        binding.dogName.setText(user.dogName)

        binding.saveBtn.setOnClickListener{
//            Log.d("lsy", "dogName: ${}")
            val user = UserModel(user.uId, binding.nickName.text.toString(), binding.dogName.text.toString(), user.dogProfile)
            val userService = (applicationContext as MyApplication).userService
            val userCall = userService.updateUser(user)
            userCall.enqueue(object : Callback<UserModel> {
                override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
                ) {
                    val user = response.body()
                    Log.d("lsy", "수정 유저: ${user}")
                    if(user != null){
                        val intent = Intent(this@EditUserActivity, MainActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@EditUserActivity, "수정 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    call.cancel()
                    Log.d("lsy", "failure 호출")
                }

            })



        }

    }
}