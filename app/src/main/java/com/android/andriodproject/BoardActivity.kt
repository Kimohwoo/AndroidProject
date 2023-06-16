package com.android.andriodproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.databinding.ActivityBoardBinding
import com.android.andriodproject.retrofit2.BoardAdapter


class BoardActivity : AppCompatActivity() {

        private lateinit var binding: ActivityBoardBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityBoardBinding.inflate(layoutInflater)
            setContentView(binding.root)



        }
    }