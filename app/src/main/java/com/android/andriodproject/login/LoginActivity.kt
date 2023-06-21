package com.android.andriodproject.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.MainActivity
import com.android.andriodproject.R
import com.android.andriodproject.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var mFirebaseAuth // 파이어베이스 인증
            : FirebaseAuth? = null
    private var mBinding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this@LoginActivity)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mBinding = ActivityLoginBinding.inflate(
            layoutInflater
        )
        val view: View = mBinding!!.root
        setContentView(view)
        mBinding!!.root.findViewById<View>(R.id.btn_login).setOnClickListener(onClickListener)
        mBinding!!.root.findViewById<View>(R.id.join_sign).setOnClickListener(onClickListener)
        mBinding!!.root.findViewById<View>(R.id.find_pwd).setOnClickListener(onClickListener)
    }

    var onClickListener = View.OnClickListener { view ->
        if (view.id == R.id.btn_login) {
            login()
        } else if (view.id == R.id.join_sign) {
            startLogin()
        } else if (view.id == R.id.find_pwd) {
            startFindID()
        }
    }

    private fun login() {
        run {

            // 로그인 요청
            val strEmail = mBinding!!.loginEmail.text.toString()
            val strPwd = mBinding!!.loginPwd.text.toString()
            mFirebaseAuth!!.signInWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) {
                        // 성공 시점
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun startLogin() {
        // 회원가입 화면으로 이동
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun startFindID() {
        // 아이디 찾기 화면 이동
//        val intent = Intent(this@LoginActivity, FindPass::class.java)
//        startActivity(intent)
    }
}