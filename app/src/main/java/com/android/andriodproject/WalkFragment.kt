package com.android.andriodproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.andriodproject.databinding.FragmentWalkBinding

@Suppress("UNREACHABLE_CODE")
class WalkFragment : Fragment() { // 시작
    private var _binding: FragmentWalkBinding? = null
    private val binding get() = _binding!!

    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWalkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeActivity.setOnClickListener {
            startActivity(Intent(activity, GoogleMapsActivity::class.java))
        }
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            binding.selectDate.text = String.format("%d년 %d월 %d일", year, month+1, dayOfMonth)
        }
        binding.writeButton.setOnClickListener{
            startActivity(Intent(activity, WriteActivity::class.java))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


}


