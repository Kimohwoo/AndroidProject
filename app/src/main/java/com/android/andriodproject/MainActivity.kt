package com.android.andriodproject

import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.android.andriodproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root

    }
    //어떻게 하면 push 가 될까요?
    // 1. 코멘트를 추가했습니다.
    // 2. 오우 씨가 현재 파일에 변경 사항을 push 했습니다.
    // 3. 성호가 현재 파일을 git push를 했는데 2번 변경사항 때문에 reject되었습니다.

}