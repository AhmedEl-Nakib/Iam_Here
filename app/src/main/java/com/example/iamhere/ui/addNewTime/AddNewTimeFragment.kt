package com.example.iamhere.ui.addNewTime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.iamhere.R
import com.example.iamhere.databinding.AddNewTimeFragmentBinding
import com.example.iamhere.ui.login.LoginViewModel
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class AddNewTimeFragment : Fragment() {
//    http://masry.live/app/json/Reservation.php?action=insert ------- ( YYYY/MM/DD attDate )
    //http://masry.live/app/json/Places.php?action=getAll
    val c = Calendar.getInstance()
    val hour = c.get(Calendar.HOUR)
    val min = c.get(Calendar.MINUTE)
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    lateinit var binding : AddNewTimeFragmentBinding
    private val viewModel: AddNewTimeViewModel by inject()
    var list = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = AddNewTimeFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
        handleObserver()
        handleClick()
    }

    private fun handleClick() {
        binding.addAppointmentBtnId.setOnClickListener {
            viewModel.addReservation(requireContext(),requireArguments().getString("UserId")!!)
        }
    }

    private fun handleObserver() {
        viewModel.placesData.observe(viewLifecycleOwner,{
            if(it.isNotEmpty()){
                it.forEach { item ->
                    list.add(item.PlaceName)
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, list)
                (binding.textField as? AutoCompleteTextView)?.setAdapter(adapter)
                (binding.textField as? AutoCompleteTextView)?.setSelection(0)
                (binding.textField as? AutoCompleteTextView)?.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, arg1, pos, id ->
                        viewModel.attPlaceId.value = it[pos].PlaceID
//                viewModel.requestModel.selection = list[pos]
                    }
            }
        })
    }

    private fun handleView() {
        viewModel.getPlaces(requireContext())
        binding.startHourTxtId.setOnClickListener {
            var Min = "0"
            var Hour = "0"
            val time = TimePickerDialog(requireActivity() , TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                Min = if(minute<10) "0$minute" else minute.toString()
                Hour = if(hourOfDay<10) "0$hourOfDay" else hourOfDay.toString()
                binding.startHourTxtId.text = "$Hour : $Min ${onTimeSet(view,hourOfDay,minute)}"
                viewModel.fromTime.value = "$hourOfDay:$minute"
            },hour,min,false)
            time.show()
        }

        binding.endHourTxtId.setOnClickListener {
            var Min = "0"
            var Hour = "0"
            val time = TimePickerDialog(requireActivity() , TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                Min = if(minute<10) "0$minute" else minute.toString()
                Hour = if(hourOfDay<10) "0$hourOfDay" else hourOfDay.toString()
                binding.endHourTxtId.text = "$Hour : $Min ${onTimeSet(view,hourOfDay,minute)}"
                viewModel.toTime.value = "$hourOfDay:$minute"
            },hour,min,false)
            time.show()
        }

        binding.dayTxtId.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                binding.dayTxtId.text = "$year-${monthOfYear+1}-$dayOfMonth"
                viewModel.date.value = "$year-${monthOfYear+1}-$dayOfMonth"
            }, year, month, day)

            dpd.datePicker.minDate = System.currentTimeMillis() - 1000

            dpd.show()
        }
    }


    private fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) : String {
        var hourOfDay = hourOfDay
        var AM_PM = " AM"
        var mm_precede = ""
        if (hourOfDay >= 12) {
            AM_PM = " PM"
            if (hourOfDay in 13..23) {
                hourOfDay -= 12
            } else {
                hourOfDay = 12
            }
        } else if (hourOfDay == 0) {
            hourOfDay = 12
        }
        if (minute < 10) {
            mm_precede = "0"
        }
        return AM_PM
    }


}