package com.android.andriodproject.retrofit2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.Model.WeatherModel
import com.android.andriodproject.databinding.ItemRetrofitBinding
import java.text.SimpleDateFormat
import java.util.Date

class MyViewHolder(val binding: ItemRetrofitBinding): RecyclerView.ViewHolder(binding.root)
class MyAdapter(val context: Context, val datas: List<WeatherModel>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = MyViewHolder(ItemRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val weather = datas?.get(position)
        val now = Date(System.currentTimeMillis())
        val timeFormat = SimpleDateFormat("hh00")
        val time = timeFormat.format(now)

        if(weather?.category == "sky" && weather?.fcstTime == time) {
            binding.sky.text = weather?.fcstValue
        }

    }

}