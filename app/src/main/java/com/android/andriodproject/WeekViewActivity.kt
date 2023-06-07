package com.example.petwalking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.andriodproject.R
import com.example.petwalking.CalendarUtils.daysInWeekArray
import com.example.petwalking.CalendarUtils.monthYearFromDate
import java.time.LocalDate

class WeekViewActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var eventListView: ListView? = null
    private var btn_previousWeekAction: Button? = null
    private var btn_nextWeekAction: Button? = null
    private var btn_newEventAction: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_view)
        calendarRecyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        monthYearText = findViewById<TextView>(R.id.tv_monthYear)
        eventListView = findViewById<ListView>(R.id.eventListView)
        btn_previousWeekAction = findViewById<Button>(R.id.btn_previousWeekAction)
        btn_previousWeekAction.setOnClickListener(View.OnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1)
            setWeekView()
        })
        btn_nextWeekAction = findViewById<Button>(R.id.btn_nextWeekAction)
        btn_nextWeekAction.setOnClickListener(View.OnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1)
            setWeekView()
        })
        btn_newEventAction = findViewById<Button>(R.id.btn_newEventAction)
        btn_newEventAction.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@WeekViewActivity,
                    EventEditActivity::class.java
                )
            )
        })
        setWeekView()
    }

    private fun setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate))
        val days: ArrayList<LocalDate> = daysInWeekArray(CalendarUtils.selectedDate)
        val calendarAdapter = CalendarAdapter(days, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
        setEventAdpater()
    }

    fun onItemClick(position: Int, date: LocalDate) {
        CalendarUtils.selectedDate = date
        setWeekView()
    }

    override fun onResume() {
        super.onResume()
        setEventAdpater()
    }

    private fun setEventAdpater() {
        val dailyEvents: ArrayList<Event> = Event.eventsForDate(CalendarUtils.selectedDate)
        val eventAdapter = EventAdapter(applicationContext, dailyEvents)
        eventListView!!.adapter = eventAdapter
    }
}