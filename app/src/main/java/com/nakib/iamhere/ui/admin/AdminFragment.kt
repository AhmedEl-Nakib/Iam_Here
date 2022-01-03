package com.nakib.iamhere.ui.admin

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.nakib.iamhere.R
import com.nakib.iamhere.databinding.FragmentAdminBinding
import com.nakib.iamhere.model.admin.AdminDoctorResponseModel
import com.nakib.iamhere.ui.doctorLocation.DoctorLocationFragment
import com.nakib.iamhere.utils.CommonUtils
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList


class AdminFragment : Fragment(), HomeAdminAdapter.OnDoctorClicked {

    lateinit var binding : FragmentAdminBinding
    private val viewModel: AdminViewModel by inject()
    lateinit var mAdapter : HomeAdminAdapter
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    lateinit var dateSelected : String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        handleObserver()
        handleClick()
    }

    private fun handleClick() {
        binding.calendarBtnId.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                viewModel.date.value = "$year-${monthOfYear+1}-$dayOfMonth"
                dateSelected = "$year-${monthOfYear+1}-$dayOfMonth"
                binding.calendarBtnId.text = "Search by Calendar ${monthOfYear+1}-$dayOfMonth"
                viewModel.getHomeList(requireContext())
//                viewModel.getHomeList(requireContext(),requireArguments().getString("UserId")!!)
            }, year, month, day)
//            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()
//            Navigation.findNavController(it).navigate(R.id.action_homeNormalFragment_to_timeTableNormalFragment)
        }
    }

    private fun init() {
        mAdapter= HomeAdminAdapter(ArrayList(),this)
        viewModel.date.value = CommonUtils.getCurrentDate()
        viewModel.getHomeList(requireContext())
    }

    private fun handleObserver() {
        viewModel.homeData.observe(viewLifecycleOwner,{
            if(it.isNotEmpty()){
                mAdapter.submitList(it as ArrayList<AdminDoctorResponseModel>)
                binding.recyclerId.adapter = mAdapter

                viewModel.searchWord.observe(viewLifecycleOwner,{ search ->
                    if(search.isNotEmpty()){
                        filter(search)
                    }else{
                        mAdapter.submitList(viewModel.homeData.value as ArrayList<AdminDoctorResponseModel>)
                    }
                })
            }else{
                mAdapter.submitList(ArrayList())
                binding.recyclerId.adapter = mAdapter
            }
//            else{
//                val list = ArrayList<AdminDoctorResponseModel>()
//                binding.recyclerId.adapter = HomeAdminAdapter(list,this)
//                binding.recyclerId.adapter!!.notifyDataSetChanged()
//            }
        })


    }

    override fun onDoctorClicked(item: AdminDoctorResponseModel) {
//        val bundle = bundleOf(
//            "lat" to item.latitude,
//            "lon" to item.longitude
//        )
//        Navigation.findNavController(requireView())
//            .navigate(R.id.action_adminFragment_to_doctorLocationFragment, bundle)
//
        requireActivity().startActivity(Intent(requireContext(),DoctorLocationFragment::class.java).putExtra("UserId",item.userId).putExtra("Date",dateSelected))
    }

    private fun filter(searchWord: String) {
        var filterdList = ArrayList<AdminDoctorResponseModel>() // result
        for(name in viewModel.homeData.value!!){
            if(name.DrName.lowercase().contains(searchWord)){
                filterdList.add(name)
            }
        }
        mAdapter.submitList(filterdList)
    }
}