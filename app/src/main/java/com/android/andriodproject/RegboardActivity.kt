package com.android.andriodproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.andriodproject.Model.BoardModel
import com.android.andriodproject.databinding.ActivityRegboardBinding
import com.android.andriodproject.retrofit2.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class RegboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegboardBinding
    private lateinit var board: BoardModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //업로딩
        binding.regButton.setOnClickListener {
            val title = binding.title.text.toString()
            val content = binding.content.text.toString()
            val author = "nickname002"

            board = BoardModel(0, title, author, content, Date(), Date(), 0)
            Log.d("lsy", "board data: ${board}")
            val boardService = (applicationContext as MyApplication).boardService
            val boardRegCall = boardService.postBoard(board)

            boardRegCall.enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    val result = response.body()
                    Log.d("lsy", "data값: ${result}")
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("lsy", "Failure 호출")
                    call.cancel()
                }
            })

        }

    }



}