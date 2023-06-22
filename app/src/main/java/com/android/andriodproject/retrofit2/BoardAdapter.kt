package com.android.andriodproject.retrofit2

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.DetailActivity
import com.android.andriodproject.Model.BoardModel
import com.android.andriodproject.Model.UserModel
import com.android.andriodproject.databinding.ItemBoardBinding

class BoardViewHolder(val binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root){

}
class BoardAdapter(val context: Context, val datas: List<BoardModel>?, val user: UserModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = BoardViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as BoardViewHolder).binding
        val board = datas?.get(position)

        Log.d("lsy", "board: ${board?.toString()}")

        binding.title.text = board?.title
        binding.author.text = board?.author

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("board", board)
            intent.putExtra("user", user)
            Log.d("lsy", "intent: ${intent}")
            context.startActivity(intent)
        }

    }


}