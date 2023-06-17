package com.android.andriodproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.android.andriodproject.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //나한테 넘어온 인텐트 객체가 가져온 Extra 데이터 받기
        var title:String? = intent.getStringExtra("title")
        var content: String? = intent.getStringExtra("content")

        binding.title.text = title
        binding.content.text = content

        binding.back.setOnClickListener {
            onBackPressed()
        }

    }
}