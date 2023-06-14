package com.android.andriodproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.andriodproject.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}