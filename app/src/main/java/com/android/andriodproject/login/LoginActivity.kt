package com.android.andriodproject.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.MainActivity
import com.android.andriodproject.Model.UserModel
import com.android.andriodproject.R
import com.android.andriodproject.databinding.ActivityLoginBinding
import com.android.andriodproject.retrofit2.MyApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var mFirebaseAuth // 파이어베이스 인증
            : FirebaseAuth? = null
    private var mDatabaseReference // 실시간 데이터 베이스
            : DatabaseReference? = null
    private var mBinding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this@LoginActivity)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("petproject")
        mBinding = ActivityLoginBinding.inflate(
            layoutInflater
        )
        val view: View = mBinding!!.root
        setContentView(view)
        mBinding!!.btnLogin.setOnClickListener {
            // 로그인 요청
            val strEmail = mBinding?.loginEmail?.text.toString()
            val strPwd = mBinding?.loginPwd?.text.toString()
            if(strEmail != null && strPwd != null){
            mFirebaseAuth!!.signInWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) {
                        val uid = mFirebaseAuth!!.currentUser?.uid
                        // 성공 시점
                        val user = UserModel(uid as String, "", "", "")
                        val userService = (applicationContext as MyApplication).userService
                        val userCall = userService.loginUser(user)
                        Log.d("lsy", "Login uId: ${user.uId}")
                        Log.d("lsy", "Login Url: ${userCall.request().url().toString()}")
                        userCall.enqueue(object :Callback<UserModel>{
                            override fun onResponse(
                                call: Call<UserModel>,
                                response: Response<UserModel>
                            ) {
                                val user = response.body()
                                Log.d("lsy", "로그인 유저: ${user}")
                                if(user != null){
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.putExtra("user", user)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                                call.cancel()
                                Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                                Log.d("lsy", "failure 호출")
                            }

                        })
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@LoginActivity, "아이디와 비밀번호를 확인해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
        mBinding!!.joinSign.setOnClickListener { // 회원가입 화면으로 이동
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        mBinding!!.findPwd.setOnClickListener { // 아이디 찾기 화면 이동
            val intent = Intent(this@LoginActivity, Change::class.java)
            startActivity(intent)
        }
    }
}