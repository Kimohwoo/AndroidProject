package com.android.andriodproject.retrofit2


import WeatherModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.Model.AirListModel.AirPollutionModel
import com.android.andriodproject.Model.WalkModel.WalkListModel
import com.android.andriodproject.databinding.ItemRetrofitBinding
import com.android.andriodproject.getTime


class MyViewHolder(val binding: ItemRetrofitBinding): RecyclerView.ViewHolder(binding.root){

}
class MyAdapter(val context: Context, val datas: List<WeatherModel>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = MyViewHolder(ItemRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val weather = datas?.get(position)

        when(weather?.category) {
            "SKY" -> binding.sky.text = "SKY: " + weather?.fcstValue
            "POP" -> binding.rainfall.text = "강수: " + weather?.fcstValue
            "TMP" -> binding.tmp2.text = "온도: " + weather?.fcstValue
            "PCP" -> binding.rainfall.text = "PCP: " + weather?.fcstValue
            "PTY" -> binding.rainfall.text = "PTY: " + weather?.fcstValue
        }
    }

}

class AirAdapter(val context: Context, val datas: List<AirPollutionModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val airPollution = datas?.get(position)
        val time = getTime("hh00")


    }

}

