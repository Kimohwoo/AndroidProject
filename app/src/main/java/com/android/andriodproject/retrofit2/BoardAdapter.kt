package com.android.andriodproject.retrofit2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.Model.BoardModel
import com.android.andriodproject.databinding.ItemBoardBinding
import com.bumptech.glide.Glide

class BoardViewHolder(val binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root) {
}

class BoardAdapter(var context: Context, var datas:MutableList<BoardModel>) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = BoardViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as BoardViewHolder).binding
        val board = datas?.get(position)

        binding.tvName.text = board?.title
        binding.tvMsg.text = board?.content
        Glide.with(context).load(board?.imgId).into(binding.iv)

    }

    // 함수 리턴 값 - 할당 연산자로 단순화
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

}