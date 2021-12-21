package com.nakib.iamhere.ui.admin

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
import org.koin.android.ext.android.inject


class AdminFragment : Fragment(), HomeAdminAdapter.OnDoctorClicked {

    lateinit var binding : FragmentAdminBinding
    private val viewModel: AdminViewModel by inject()
    lateinit var mAdapter : HomeAdminAdapter
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
    }

    private fun init() {
        mAdapter= HomeAdminAdapter(ArrayList(),this)
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
            }
//            else{
//                val list = ArrayList<AdminDoctorResponseModel>()
//                binding.recyclerId.adapter = HomeAdminAdapter(list,this)
//                binding.recyclerId.adapter!!.notifyDataSetChanged()
//            }
        })


    }

    override fun onDoctorClicked(item: AdminDoctorResponseModel) {
        val bundle = bundleOf(
            "lat" to item.latitude,
            "lon" to item.longitude
        )
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminFragment_to_doctorLocationFragment, bundle)
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