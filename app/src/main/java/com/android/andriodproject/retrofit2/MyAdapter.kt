package com.android.andriodproject.retrofit2


import WeatherModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.Model.AirListModel.AirPollutionModel
import com.android.andriodproject.databinding.ItemRetrofitBinding
import com.android.andriodproject.getTime


class MyViewHolder(val binding: ItemRetrofitBinding): RecyclerView.ViewHolder(binding.root){
//    fun ViewHolder(v: View) {
//        super(v)
//        view = v
//        view.setOnClickListener(View.OnClickListener {
//            // item clicked
//        })
//    }
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
        val time = getTime("hh00")

        binding.sky.text = weather?.fcstValue

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

//        binding.rainfall.text = airPollution?.sidoName
//        binding.temperature.text = airPollution?.khaiGrade

    }

}