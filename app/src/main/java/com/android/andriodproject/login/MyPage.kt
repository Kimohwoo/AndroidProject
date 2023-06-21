package com.android.andriodproject.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.R
import com.android.andriodproject.databinding.ActivityMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MyPage : AppCompatActivity() {
    private var user: FirebaseUser? = null
    private var userAuth: FirebaseAuth? = null
    private var mBinding: ActivityMyPageBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        user = FirebaseAuth.getInstance().currentUser
        userAuth = FirebaseAuth.getInstance()
        mBinding = ActivityMyPageBinding.inflate(
            layoutInflater
        )
        //        View view = mBinding.getRoot();
//        setContentView(view);
        mBinding!!.btnPrivCheck.setOnClickListener { }
    }
}