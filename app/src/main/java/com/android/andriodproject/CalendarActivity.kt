package com.android.andriodproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.Model.WalkModel.WalkModel
import com.android.andriodproject.databinding.ActivityCalendarBinding
import com.android.andriodproject.databinding.ItemCalendarListBinding
import com.android.andriodproject.retrofit2.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CalendarActivity : AppCompatActivity() {

    lateinit var binding : ActivityCalendarBinding

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
            binding.trackingDistance.text= walk?.distance
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.changeActivity.setOnClickListener {
            startActivity(Intent(this, GoogleMapsActivity::class.java))
        }

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            binding.selectDate.text = String.format("%d년 %d월 %d일", year, month+1, dayOfMonth)
        }

        binding.calendarRecycle.layoutManager = LinearLayoutManager(this)

        //test, MyApplication 에서 만든 형으로 다운캐스팅!! 해당 객체 생성!
        val customWalkApi = (applicationContext as MyApplication).walkService

        //서비스 인터페이스에 정의된 추상 함수를 MyApplication 클래스에 붙여서 사용
        val walkListCall = customWalkApi.searchAllData("nsnsh@naver.com")
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
                    binding.calendarRecycle.adapter =CalendarAdapter(walkList)
                }

                override fun onFailure(call: Call<List<WalkModel>>, t: Throwable) {
                    Log.d("nsh","실패")
                }
            }
        )

    }
}