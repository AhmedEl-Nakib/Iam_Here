package com.example.iamhere.ui.splash

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.iamhere.R
import com.example.iamhere.databinding.SplashFragmentBinding
import com.example.iamhere.utils.CommonUtils

class SplashFragment : Fragment() {

    lateinit var binding : SplashFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SplashFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({ Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment) }, 3000)
//        handleSharedPref()
    }

    private fun handleSharedPref() {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("iamhere", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("CurrentDate",CommonUtils.getCurrentDate())
        editor.apply()

        Toast.makeText(requireContext(),sharedPreferences.getString("CurrentDate","Def"),Toast.LENGTH_SHORT).show()
    }
}