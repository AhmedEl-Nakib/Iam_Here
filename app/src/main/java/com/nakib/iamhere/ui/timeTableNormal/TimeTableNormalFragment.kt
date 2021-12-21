package com.nakib.iamhere.ui.timeTableNormal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nakib.iamhere.databinding.TimeTableNormalFragmentBinding

import com.applandeo.materialcalendarview.EventDay

import com.nakib.iamhere.R
import java.util.*
import kotlin.collections.ArrayList


class TimeTableNormalFragment : Fragment() {

    lateinit var binding : TimeTableNormalFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = TimeTableNormalFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
    }

    private fun handleView() {
        val events: MutableList<EventDay> = ArrayList()

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        events.add(EventDay(calendar, R.drawable.ic_dot))
        binding.calendarView.setEvents(events)

//        calendar.set(2021, 10, 27)

//        binding.calendarView.setDate(calendar)
    }


}