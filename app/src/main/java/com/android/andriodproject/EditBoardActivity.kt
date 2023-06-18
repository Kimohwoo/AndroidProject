package com.android.andriodproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.andriodproject.Model.BoardModel
import com.android.andriodproject.databinding.ActivityEditBoardBinding
import com.android.andriodproject.retrofit2.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class EditBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBoardBinding
    private lateinit var board: BoardModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        board = intent.getSerializableExtra("board") as BoardModel

        binding.title.setText(board.title)
        binding.content.setText(board.content)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.editBtn.setOnClickListener {
            board.title = binding.title.text.toString()
            board.content = binding.content.text.toString()

            Log.d("lsy", "upBoard 값 : ${board}")
            val boardService = (applicationContext as MyApplication).boardService
            val boardCall = boardService.putBoard(board.no, board)
            Log.d("lsy", "게시판 수정 Url: " + boardCall.request().url().toString())
            boardCall.enqueue(object : Callback<Long>{
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    val response = response.body()
                    Log.d("lsy", "업데이트: ${response}")
                    if(response != -1L) {
                        intent = Intent(this@EditBoardActivity, DetailActivity::class.java)
                        intent.putExtra("board", board)
                        startActivity(intent)
                        Toast.makeText(this@EditBoardActivity, "게시물 수정", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@EditBoardActivity, "수정 실패!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Long>, t: Throwable) {
                    call.cancel()
                }

            })
        }

    }
}