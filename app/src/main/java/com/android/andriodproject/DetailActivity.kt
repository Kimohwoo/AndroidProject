package com.android.andriodproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.android.andriodproject.Model.BoardListModel
import com.android.andriodproject.Model.BoardModel
import com.android.andriodproject.Model.UserModel
import com.android.andriodproject.databinding.ActivityDetailBinding
import com.android.andriodproject.retrofit2.MyApplication
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //툴바
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.mainDrawerView.setNavigationItemSelectedListener {
                menuItem ->
            when(menuItem.itemId){
                R.id.excerciseBtn -> {
                    val intent = Intent(applicationContext, GoogleMapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.weatherBtn -> {
                    startActivity(Intent(applicationContext, WeatherActivity::class.java))
                    true
                }
                R.id.boardBtn -> {
                    startActivity(Intent(applicationContext, BoardActivity::class.java))
                    true
                }
                else -> false
            }
        }

        //나한테 넘어온 인텐트 객체가 가져온 Extra 데이터 받기
        var board: BoardModel = intent.getSerializableExtra("board") as BoardModel
        val user = intent.getSerializableExtra("user") as UserModel
        Log.d("lsy", "board: ${board}")
        binding.title.text = board.title
        binding.content.text = board.content

        binding.back.setOnClickListener {
            intent = Intent(this@DetailActivity, BoardActivity::class.java)
            startActivity(intent)
        }

        Log.d("lsy", "author: ${board.author}")

        if(board.author == "nickname002"){
            binding.updateBtn.visibility = View.VISIBLE
            binding.deleteBtn.visibility = View.VISIBLE
        }

        binding.deleteBtn.setOnClickListener {
            val boardService = (applicationContext as MyApplication).boardService
            val deleteBoardCall = boardService.deleteBoard(board.no)
            deleteBoardCall.enqueue(object : Callback<Long>{
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    val response = response.body()
                    Log.d("lsy", "response: ${response}")
                    if(response == 1L) {
                        intent = Intent(this@DetailActivity, BoardActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<Long>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

        binding.updateBtn.setOnClickListener {
            intent = Intent(this@DetailActivity, EditBoardActivity::class.java)
            intent.putExtra("board", board)
            startActivity(intent)
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //이벤트가 toggle 버튼에서 제공된거라면..
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}