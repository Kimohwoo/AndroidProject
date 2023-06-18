package com.android.andriodproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.Model.BoardListModel
import com.android.andriodproject.Model.BoardModel
import com.android.andriodproject.databinding.ActivityBoardBinding
import com.android.andriodproject.retrofit2.BoardAdapter
import com.android.andriodproject.retrofit2.BoardService
import com.android.andriodproject.retrofit2.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardBinding
    private var pageNo = 1
    private var numOfRows = 150
    private lateinit var boardService: BoardService
    private var item: List<BoardModel>? = null
    private lateinit var myBoardBtn: Button
    private lateinit var allboardBtn: Button

    val recycler: RecyclerView by lazy {
        binding.boardRecycler
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        boardService = (applicationContext as MyApplication).boardService
        recycler.layoutManager = layoutManager

        myBoardBtn = binding.myBoardBtn
        myBoardBtn.visibility = View.VISIBLE
        allboardBtn = binding.allBoardBtn
        allboardBtn.visibility = View.INVISIBLE

        //내 글 보기
        binding.myBoardBtn.setOnClickListener {
            val myItem = item?.filter {
                it.author == "nickname002"
            }
            Log.d("lsy", "myItem 확인 : ${myItem}")
            recycler.adapter = BoardAdapter(this@BoardActivity, myItem)
            allboardBtn.visibility = View.VISIBLE
            allboardBtn.isEnabled = true
            myBoardBtn.visibility = View.INVISIBLE
            myBoardBtn.isEnabled = false
        }

        binding.allBoardBtn.setOnClickListener {
            recycler.adapter =
                BoardAdapter(this@BoardActivity, item)
            myBoardBtn.isEnabled = true
            myBoardBtn.visibility = View.VISIBLE
            allboardBtn.isEnabled = false
            allboardBtn.visibility = View.INVISIBLE
        }

        //regButton
        binding.regButton.setOnClickListener {
            val intent = Intent(this, RegboardActivity::class.java)
            startActivity(intent)
        }

        //전체글 -> 현재 스크롤링 전단계
        val boardListCall = boardService.getBoardList(pageNo, numOfRows)
        Log.d("lsy", "boardList url: " + boardListCall.request().url().toString())
        boardListCall.enqueue(object : Callback<BoardListModel> {
            override fun onResponse(
                call: Call<BoardListModel>,
                response: Response<BoardListModel>
            ) {
                val boardList = response.body()
                item = boardList?.item
                Log.d("lsy", "사이즈 : ${item?.size}, data값: ${item}")
                recycler.adapter =
                    BoardAdapter(this@BoardActivity, item)
            }
            override fun onFailure(call: Call<BoardListModel>, t: Throwable) {
                Log.d("lsy", "Failure 호출")
                call.cancel()
            }
        })

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = layoutManager!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1
                Log.d("lsy", "last: ${lastVisibleItemPosition}")
                Log.d("lsy", "itemTotal = ${itemTotalCount}")
                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition == itemTotalCount) {
                    Log.d("lsy", "스크롤 끝")
                }
            }
        })
    }

//    override fun onResume() {
//        super.onResume()
//
//        //보통 이곳에서 데이터들을 갱신하는 작업들이 이루어짐
//        // adapter은 nullable 변수임 따라서 ? 연산자 붙이기
//        recycler.adapter?.notifyDataSetChanged()
//
//        //recycler.adapter!!.notifyDataSetChanged() //무조건 널이 아니다 연산자
//    }
//
//    //옵션 메뉴 만드는 콜백 메소드
////    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
////        //자바처럼 menuInflater를 get하는 작업필요없이 액티비티에 이미 menuInflater 객체 존재
////        menuInflater.inflate(R.menu.option, menu)
////
////        return super.onCreateOptionsMenu(menu)
////    }
//
//    //옵션 메뉴 아이템 선택하면 자동으로 발동하는 콜백 메소드
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        when(item.itemId) {
//            R.id.content -> Toast.makeText(this, "aaa 클릭", Toast.LENGTH_SHORT).show()
//            R.id.author -> {
//                Toast.makeText(this, "bbb 클릭", Toast.LENGTH_SHORT).show()
//            }
////            R.id. -> Toast.makeText(this, "ccc 클릭", Toast.LENGTH_SHORT).show()
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

}