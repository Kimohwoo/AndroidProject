package com.android.andriodproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.andriodproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private var clicked = false

    companion object {
        const val uid = "abcdefg"
        const val nickname = "Samkim"
        const val dogname = "똘똘이"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addBtn.setOnClickListener {
            onAddButtonClicked()
        }
        binding.cameraBtn.setOnClickListener {
            Toast.makeText(this, "플로팅1", Toast.LENGTH_SHORT).show()
        }
        binding.galleryBtn.setOnClickListener {
            Toast.makeText(this, "플로팅2", Toast.LENGTH_SHORT).show()
        }
        binding.exerciseBtn.setOnClickListener {
            Toast.makeText(this, "플로팅3", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, GoogleMapsActivity::class.java)
            intent.putExtra(GoogleMapsActivity.uid, "$uid")
            startActivity(intent)
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.cameraBtn.visibility = View.GONE
            binding.galleryBtn.visibility = View.GONE
            binding.exerciseBtn.visibility = View.GONE
            binding.cameraBtn.isEnabled = true
            binding.galleryBtn.isEnabled = true
            binding.exerciseBtn.isEnabled = true
        } else {
            binding.cameraBtn.isEnabled = false
            binding.galleryBtn.isEnabled = false
            binding.exerciseBtn.isEnabled = false
            binding.cameraBtn.visibility = View.VISIBLE
            binding.galleryBtn.visibility = View.VISIBLE
            binding.exerciseBtn.visibility = View.VISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.cameraBtn.startAnimation(fromBottom)
            binding.galleryBtn.startAnimation(fromBottom)
            binding.exerciseBtn.startAnimation(fromBottom)
            binding.addBtn.startAnimation(rotateOpen)
        } else {
            binding.cameraBtn.startAnimation(toBottom)
            binding.galleryBtn.startAnimation(toBottom)
            binding.exerciseBtn.startAnimation(toBottom)
            binding.addBtn.startAnimation(rotateClose)
        }
    }
}
