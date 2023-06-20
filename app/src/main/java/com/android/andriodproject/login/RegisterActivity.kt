package com.android.andriodproject.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.R
import com.android.andriodproject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private var mFirebaseAuth // 파이어베이스 인증
            : FirebaseAuth? = null
    private var mDatabaseReference // 실시간 데이터 베이스
            : DatabaseReference? = null
    private var mRegister: ActivityRegisterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("androidproject")
        mRegister = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mRegister!!.root)

        mRegister!!.btnJoin.setOnClickListener {
            // 회원가입 처리시작
            val strEmail = mRegister!!.joinEmail.text.toString()
            val strPwd = mRegister!!.joinPwd.text.toString()
            Log.d("lsy", "email: ${strEmail},pwd: ${strPwd}")
            mFirebaseAuth!!.createUserWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener(this@RegisterActivity) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mFirebaseAuth!!.currentUser
                        val account = UserAccount()
                        account.idToken = firebaseUser!!.uid
                        account.emailId = firebaseUser.email
                        account.password = strPwd

                        // setValue : database에 insert 하는 방식
                        mDatabaseReference!!.child("UserAccount").child(firebaseUser.uid)
                            .setValue(account)
                        Toast.makeText(this@RegisterActivity, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@RegisterActivity, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
        mRegister!!.btnCancel.setOnClickListener { //로그인 화면으로 이동
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}