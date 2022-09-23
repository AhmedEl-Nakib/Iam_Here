package com.nakib.iamhere.ui.updatePassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nakib.iamhere.databinding.UpdatePasswordFragmentBinding
import com.nakib.iamhere.ui.login.LoginViewModel
import org.koin.android.ext.android.inject


class UpdatePasswordFragment : Fragment() {

    lateinit var binding : UpdatePasswordFragmentBinding
    private val viewModel: UpdatePasswordViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = UpdatePasswordFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
        handleClick()
    }

    private fun handleClick() {
        binding.updatePasswordId.setOnClickListener {
            viewModel.updatePassword(requireContext(),requireArguments().getString("UserId")!!,requireArguments().getString("DrName")!!)
        }
    }

    private fun handleView() {

    }


}