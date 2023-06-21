package com.android.andriodproject.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.R
import com.google.firebase.auth.FirebaseAuth

class Change : AppCompatActivity() {
    private var mFirebaseAuth: FirebaseAuth? = null
    private val btn_reset_check: Button? = null
    private val btn_reset_cancel: Button? = null
    private val check_reset_email: EditText? = null
    private val check_rest_pwd: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)
        mFirebaseAuth = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        findViewById<View>(R.id.btn_reset_check).setOnClickListener {
            if (user != null) {
                // Name, email address, and profile photo Url
                val email = user.email

                // Check if user's email is verified
                val emailVerified = user.isEmailVerified

                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getIdToken() instead.
                val uid = user.uid
                Toast.makeText(this@Change, "아이디 비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
            } else if (user != null) {
                for (profile in user.providerData) {
                    // Id of the provider (ex: google.com)
                    val providerId = profile.providerId

                    // UID specific to the provider
                    val uid = profile.uid

                    // Name, email address, and profile photo Url
                    val name = profile.displayName
                    val email = profile.email
                }
            } else {
                Toast.makeText(this@Change, "회원가입을 해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}