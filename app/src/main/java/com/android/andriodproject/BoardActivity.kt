package com.android.andriodproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.Model.BoardListModel
import com.android.andriodproject.Model.BoardModel
import com.android.andriodproject.Model.UserModel
import com.android.andriodproject.databinding.ActivityBoardBinding
import com.android.andriodproject.databinding.ItemBoardBinding
import com.android.andriodproject.retrofit2.BoardAdapter
import com.android.andriodproject.retrofit2.BoardService
import com.android.andriodproject.retrofit2.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardBinding
    private var pageNo = 1
    private var numOfRows = 10
    private lateinit var boardService: BoardService
    private var item: MutableList<BoardModel>? = null
    private lateinit var myBoardBtn: Button
    private lateinit var allboardBtn: Button
    lateinit var toggle: ActionBarDrawerToggle
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

        val user = intent.getSerializableExtra("user") as UserModel


        //툴바
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.mainDrawerView.setNavigationItemSelectedListener {
                menuItem ->
            when(menuItem.itemId){
                R.id.excerciseBtn -> {
                    val intent = Intent(applicationContext, GoogleMapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.weatherBtn -> {
                    startActivity(Intent(applicationContext, WeatherActivity::class.java))
                    true
                }
                R.id.boardBtn -> {
                    startActivity(Intent(applicationContext, BoardActivity::class.java))
                    true
                }
                else -> false
            }
        }

//        Log.d("lsy", "user: ${user}")
        //내 글 보기
        binding.myBoardBtn.setOnClickListener {
            val boardListCall = boardService.getMyList(user.uId)
            Log.d("lsy", "boardList url: " + boardListCall.request().url().toString())
            boardListCall.enqueue(object : Callback<BoardListModel> {
                override fun onResponse(
                    call: Call<BoardListModel>,
                    response: Response<BoardListModel>
                ) {
                    val boardList = response.body()
                    val myItem = boardList?.item
                    if(myItem != null) {
                        Log.d("lsy", "myItem 값: ${myItem}")
                        recycler.adapter =
                            BoardAdapter(this@BoardActivity, myItem)
                    } else {
                        Toast.makeText(this@BoardActivity, "작성한 게시글이 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BoardListModel>, t: Throwable) {
                    Log.d("lsy", "Failure 호출")
                    call.cancel()
                }
            })
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
            if(user.nickName != null && user.nickName != "") {
                intent = Intent(this, RegboardActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            } else {
                Toast.makeText(this@BoardActivity, "닉네임이 없습니다!", Toast.LENGTH_SHORT).show()
                intent = Intent(this@BoardActivity, EditUserActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        }
        //전체글
        val boardListCall = boardService.getBoardList(pageNo, numOfRows)
        Log.d("lsy", "boardList url: " + boardListCall.request().url().toString())
        var call = boardListCall.enqueue(object : Callback<BoardListModel> {
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

        if(myBoardBtn.visibility == View.VISIBLE) {
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition =
                        layoutManager!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
                    val itemTotalCount =
                        recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1
//                Log.d("lsy", "last: ${lastVisibleItemPosition}")
//                Log.d("lsy", "itemTotal = ${itemTotalCount}")
                    // 스크롤이 끝에 도달했는지 확인
                    if (lastVisibleItemPosition == itemTotalCount){
//                        isLoading = true
                        Log.d("lsy", "스크롤 끝")
                        pageNo += 1
                        Log.d("lsy", "pageNO : ${pageNo}, numOfRows: ${numOfRows}")
                        val boardListCall = boardService.getBoardList(pageNo, numOfRows)
                        boardListCall.enqueue(object : Callback<BoardListModel> {
                            override fun onResponse(
                                call: Call<BoardListModel>,
                                response: Response<BoardListModel>
                            ) {
                                val response = response.body()

                                if(response != null && response.item.isNotEmpty() ) {
                                    Log.d("lsy", "item 값: ${item}")
                                    getMoreData(response?.item)
                                }

                            }
                            override fun onFailure(call: Call<BoardListModel>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            })
        }
    }

    private fun getMoreData(data: MutableList<BoardModel>?) {
        if (data != null) {
            Log.d("lsy","data의 갯수 : ${data.size}")
        }
        // 9
        binding.boardRecycler.adapter?.notifyItemInserted(item?.size?.minus(1)?: 0)
//        item?.removeAt(item?.size?.minus(1)?: 0)
        //9
//        item?.removeAt(item?.size?.minus(2)?: 0)

        //10
        val currentSize = item?.size
        if (currentSize != null) {

            //Log.d("lsy","data 0 조회 : ${data?.get(0)}")
            //Log.d("lsy","data 9 조회 : ${data?.get(9)}")
            item?.addAll(data as Collection<BoardModel>)
        }
        binding.boardRecycler.adapter?.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //이벤트가 toggle 버튼에서 제공된거라면..
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}