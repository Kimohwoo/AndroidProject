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
            "SKY" -> {
                when(weather?.fcstValue){
                    "1" -> binding.sky.text = "맑음"
                    "3" -> binding.sky.text = "구름많음"
                    "4" -> binding.sky.text = "흐림"
                }
            }
            "POP" -> binding.rainfall.text = "강수확률: " + weather?.fcstValue + "%"
            "TMP" -> binding.tmp2.text = "기온: " + weather?.fcstValue
            "PCP" -> {
                when(weather?.fcstValue){
                    "강수없음" -> binding.pcp.text = weather?.fcstValue
                    else -> binding.pcp.text = "강수량: " + weather?.fcstValue + "mm"
                }
            }
            "PTY" -> {
                when(weather?.fcstValue){
                    "0" -> binding.pty.text = "화창한 날씨"
                    "1" -> binding.pty.text = "비"
                    "2" -> binding.pty.text = "비 또는 눈"
                    "3" -> binding.pty.text = "눈"
                    "4" -> binding.pty.text = "소나기"
                }
            }
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
//        val time = getTime("hh00")


    }

}

