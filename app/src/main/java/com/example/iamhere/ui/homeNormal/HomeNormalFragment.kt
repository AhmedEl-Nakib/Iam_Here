package com.example.iamhere.ui.homeNormal

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.iamhere.MainActivity
import com.example.iamhere.R
import com.example.iamhere.databinding.HomeNormalFragmentBinding
import com.example.iamhere.ui.addNewTime.AddNewTimeViewModel
import com.example.iamhere.utils.CommonUtils
import org.koin.android.ext.android.inject
import java.util.*

class HomeNormalFragment : Fragment() {

    lateinit var binding : HomeNormalFragmentBinding
    private val viewModel: HomeNormalViewModel by inject()
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = HomeNormalFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("iamhere", Context.MODE_PRIVATE)

        handleView()
        handleClick()
        handleObserver()
    }

    private fun handleObserver() {
        viewModel.homeData.observe(viewLifecycleOwner,{
            if(it.isNotEmpty()){
                binding.tableId.adapter = HomeNormalTableAdapter(it)
            }
        })
    }

    private fun handleClick() {

        binding.calendarBtnId.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                viewModel.date.value = "$year-${monthOfYear+1}-$dayOfMonth"
                viewModel.getHomeList(requireContext(),requireArguments().getString("UserId")!!)
            }, year, month, day)
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()
//            Navigation.findNavController(it).navigate(R.id.action_homeNormalFragment_to_timeTableNormalFragment)
        }

        binding.allowLocationBtnId.setOnClickListener {
            (activity as MainActivity?)!!.getStarted(requireArguments().getString("UserId")!!)
            binding.allowLocationBtnId.isEnabled = false
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("CurrentDate", CommonUtils.getCurrentDate())
            editor.apply()
            Toast.makeText(requireContext(),"Thanks for attendance",Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleView() {
//        binding.tableId.adapter = HomeNormalTableAdapter()
        if(requireArguments().getString("ViewType").equals("Admin")){
//            http://masry.live/app/json/Attendance.php?action=getAllAttendance for table
            binding.addNewBtnId.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_homeNormalFragment_to_doctorLocationFragment)
            }
            binding.addNewBtnId.text = "Search for members"
        }else{
//            http://masry.live/app/json/Attendance.php?action=GetUserAttendance
            binding.addNewBtnId.setOnClickListener {
                val bundle = bundleOf( "UserId" to requireArguments().getString("UserId"))
                Navigation.findNavController(it).navigate(R.id.action_homeNormalFragment_to_addNewTimeFragment,bundle)
            }
            binding.addNewBtnId.text = "Add new"
            binding.drNameId.text = "Welcome Dr : ${requireArguments().getString("DrName")}"
            viewModel.date.value = CommonUtils.getCurrentDate()
            viewModel.getHomeList(requireContext(),requireArguments().getString("UserId")!!)


            if(sharedPreferences.getString("CurrentDate","Def").equals(CommonUtils.getCurrentDate())){
                binding.allowLocationBtnId.isEnabled = false
            }else {

            }
//            Toast.makeText(requireContext(),sharedPreferences.getString("CurrentDate","Def"), Toast.LENGTH_SHORT).show()

        }
    }

}