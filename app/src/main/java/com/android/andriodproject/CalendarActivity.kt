package com.android.andriodproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.Model.UserModel
import com.android.andriodproject.Model.WalkModel.WalkModel
import com.android.andriodproject.databinding.ActivityCalendarBinding
import com.android.andriodproject.databinding.ItemCalendarListBinding
import com.android.andriodproject.login.LoginActivity
import com.android.andriodproject.retrofit2.MyApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CalendarActivity : AppCompatActivity() {

    lateinit var binding : ActivityCalendarBinding
    lateinit var walkListCall: Call<List<WalkModel>>
    lateinit var dayWalkListCall: Call<List<WalkModel>>
    lateinit var toggle: ActionBarDrawerToggle
    private var mFirebaseAuth: FirebaseAuth? = Firebase.auth

    class MyViewHolder (val binding: ItemCalendarListBinding) : RecyclerView.ViewHolder(binding.root)

    class CalendarAdapter(val datas: List<WalkModel>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
                = MyViewHolder(ItemCalendarListBinding.inflate(LayoutInflater.from(parent.context),parent,false))

        override fun getItemCount(): Int {
            Log.d("nsh","init datas size 크기: ${datas?.size}")
            return datas?.size?:0
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val binding=(holder as MyViewHolder).binding
            val walk=datas?.get(position)

            val speed = Converter.calculateSpeed(walk?.distance.toString(),
                walk?.exerciseTime.toString()
            )
            val formattedSpeed = speed?.let { String.format("%.2f", it) }

            binding.trackingDate.text= walk?.exerciseId.toString()
            binding.trackingDistance.text= "${walk?.distance} km"
            binding.trackingTime.text=walk?.exerciseTime
            binding.trackingCalories.text="${walk?.calorie} kcal/h"
            binding.trackingAvgSpeed.text = "${formattedSpeed} km/h"



        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR) % 100
        var dayNum = String.format("%02d%02d%02d", year, month, dayOfMonth)
//        val uid = intent.getStringExtra("uid") as String
//        val uid = "user001"

        binding.calendarRecycle.layoutManager = LinearLayoutManager(this)

        //툴바

        val user = intent.getSerializableExtra("user") as UserModel

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.mainDrawerView.setNavigationItemSelectedListener {
                menuItem ->
            when(menuItem.itemId){
                R.id.excerciseBtn -> {
                    val intent = Intent(applicationContext, GoogleMapsActivity::class.java)
                    intent.putExtra("uid", "${user.uId}")
                    intent.putExtra("user", user)
                    Log.d("lsy", "user: ${user.uId}")
                    startActivity(intent)
                    true
                }
                R.id.weatherBtn -> {
                    intent = Intent(this@CalendarActivity, WeatherActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    true
                }
                R.id.boardBtn -> {
                    intent = Intent(this@CalendarActivity, BoardActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    true
                }
                R.id.btn_logout -> {
                    mFirebaseAuth!!.signOut()
                    intent = Intent(this@CalendarActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.editBtn -> {
                    intent = Intent(this@CalendarActivity, EditUserActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.home -> {
                    intent = Intent(this@CalendarActivity, MainActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        //

        //test, MyApplication 에서 만든 형으로 다운캐스팅!! 해당 객체 생성!
        val customWalkApi = (applicationContext as MyApplication).walkService

        //서비스 인터페이스에 정의된 추상 함수를 MyApplication 클래스에 붙여서 사용
        val walkListCall = customWalkApi.searchAllData(user.uId)
        Log.d("nsh", "url:" + walkListCall.request().url().toString())
        //실제 통신을 해서 사용하기. 서버와 통신해서, 데이터를 받는다.
        walkListCall.enqueue(
            object: Callback<List<WalkModel>> {
                override fun onResponse(
                    call: Call<List<WalkModel>>,
                    response: Response<List<WalkModel>>
                ) {
                    val walkList = response.body()
                    Log.d("nsh","walkList(response.body())의 값: ${walkList}")

                }

                override fun onFailure(call: Call<List<WalkModel>>, t: Throwable) {
                    Log.d("nsh","실패")
                }
            }
        )

        binding.changeActivity.setOnClickListener {
//            intent = Intent(this@CalendarActivity, ResultActivity::class.java)
//            intent.putExtra()
//            startActivity()
        }

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            binding.selectDate.text = String.format("%d년 %d월 %d일", year, month+1, dayOfMonth)
            val updatedDayNum = String.format("%02d%02d%02d", year % 100, month + 1, dayOfMonth)

            dayWalkListCall = customWalkApi.searchData(user.uId, updatedDayNum)
            Log.d("nsh", "url:" + dayWalkListCall.request().url().toString())

            dayWalkListCall.enqueue(object : Callback<List<WalkModel>> {
                override fun onResponse(
                    call: Call<List<WalkModel>>,
                    response: Response<List<WalkModel>>
                ) {
                    val updatedDayWalkList = response.body()
                    Log.d("nsh", "updatedDayWalkList(response.body())의 값: $updatedDayWalkList")
                    binding.calendarRecycle.adapter = CalendarActivity.CalendarAdapter(updatedDayWalkList)
                }

                override fun onFailure(call: Call<List<WalkModel>>, t: Throwable) {
                    Log.d("nsh", "실패")
                }
            })
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