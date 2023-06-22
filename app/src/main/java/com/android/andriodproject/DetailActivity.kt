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
import com.android.andriodproject.login.LoginActivity
import com.android.andriodproject.retrofit2.MyApplication
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    lateinit var toggle: ActionBarDrawerToggle
    private var mFirebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var board: BoardModel = intent.getSerializableExtra("board") as BoardModel
        val user = intent.getSerializableExtra("user") as UserModel

        //툴바
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.mainDrawerView.setNavigationItemSelectedListener {
                menuItem ->
            when(menuItem.itemId){
                R.id.excerciseBtn -> {
                    intent = Intent(applicationContext, GoogleMapsActivity::class.java)
                    intent.putExtra("uid", "${user.uId}")
                    intent.putExtra("user", user)
                    Log.d("lsy", "user: ${user.uId}")
                    startActivity(intent)
                    true
                }
                R.id.weatherBtn -> {
                    intent = Intent(this@DetailActivity, WeatherActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    true
                }
                R.id.boardBtn -> {
                    intent = Intent(this@DetailActivity, BoardActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    true
                }
                R.id.btn_logout -> {
                    mFirebaseAuth!!.signOut()
                    intent = Intent(this@DetailActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.editBtn -> {
                    intent = Intent(this@DetailActivity, EditUserActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.home -> {
                    intent = Intent(this@DetailActivity, MainActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        //나한테 넘어온 인텐트 객체가 가져온 Extra 데이터 받기
        Log.d("lsy", "board: ${board}")
        binding.title.text = board.title
        binding.content.text = board.content

        binding.back.setOnClickListener {
            intent = Intent(this@DetailActivity, BoardActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("board", board)
            startActivity(intent)
        }

        if(board.author == user.nickName){
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
                        intent.putExtra("user", user)
                        intent.putExtra("board", board)
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
            intent.putExtra("user", user)
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