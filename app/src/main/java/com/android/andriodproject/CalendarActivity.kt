package com.android.andriodproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.andriodproject.Model.WalkModel.WalkListModel
import com.android.andriodproject.databinding.ActivityCalendarBinding
import com.android.andriodproject.retrofit2.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.stream.Stream


class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding= ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.changeActivity.setOnClickListener {
            startActivity(Intent(this, GoogleMapsActivity::class.java))
        }

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyMMdd", Locale.getDefault())
            val dayNum = dateFormat.format(calendar.time)
            binding.selectDate.text = String.format("%d년 %d월 %d일", year, month+1, dayOfMonth)
        }


        //test, MyApplication 에서 만든 형으로 다운캐스팅!! 해당 객체 생성!
        val customWalkApi = (applicationContext as MyApplication).walkService

        //서비스 인터페이스에 정의된 추상 함수를 MyApplication 클래스에 붙여서 사용
        val walkListCall = customWalkApi.searchAllData("nsnsh@naver.com")
        Log.d("nsh", "url:" + walkListCall.request().url().toString())
        //실제 통신을 해서 사용하기. 서버와 통신해서, 데이터를 받는다.
        walkListCall.enqueue(
            object: Callback<WalkListModel> {
                override fun onResponse(
                    call: Call<WalkListModel>,
                    response: Response<WalkListModel>
                ) {
                    val walkList = response.body()
                    Log.d("nsh","walkList(response.body())의 값: ${walkList?.items}")
                }

                override fun onFailure(call: Call<WalkListModel>, t: Throwable) {
                }
            }
        )

    }


}
