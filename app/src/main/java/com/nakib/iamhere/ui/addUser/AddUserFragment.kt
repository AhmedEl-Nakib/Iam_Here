package com.nakib.iamhere.ui.addUser

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nakib.iamhere.R
import com.nakib.iamhere.databinding.AddUserFragmentBinding
import com.nakib.iamhere.databinding.FragmentAdminBinding
import com.nakib.iamhere.ui.admin.AdminViewModel
import org.koin.android.ext.android.inject

class AddUserFragment : Fragment() {

    lateinit var binding : AddUserFragmentBinding
    private val viewModel: AddUserViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddUserFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
        handleClick()
        handleObserver()
    }

    private fun handleView() {
//        requireArguments().getString("UserId")!!
        if(requireArguments().getString("ViewType") == "UpdateUser"){
            binding.userIdOuterId.visibility = View.VISIBLE
            binding.addNewUserBtnId.text = "Update User"
            binding.userIdInnerId.setText(requireArguments().getString("UserId"))
            binding.userIdOuterId.isEnabled = false
        }

        binding.isAdminCheckBoxId.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                viewModel.isAdmin.value = "1"
            }else{
                viewModel.isAdmin.value = "0"
            }
        }
    }

    private fun handleObserver() {

    }

    private fun handleClick() {
        binding.addNewUserBtnId.setOnClickListener {
            if(requireArguments().getString("ViewType") == "UpdateUser") {
                viewModel.updateUser(requireContext())
            }else{
                viewModel.addUser(requireContext())

            }
        }
    }


}