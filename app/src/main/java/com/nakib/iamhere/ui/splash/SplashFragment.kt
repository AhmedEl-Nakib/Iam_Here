package com.nakib.iamhere.ui.splash

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.nakib.iamhere.R
import com.nakib.iamhere.databinding.SplashFragmentBinding
import com.nakib.iamhere.utils.CommonUtils

class SplashFragment : Fragment() {

    lateinit var binding : SplashFragmentBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SplashFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("iamhere", Context.MODE_PRIVATE)

        Handler().postDelayed({
            if(sharedPreferences.getString("IsFirstTime","").equals("true")) {
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment)
            }else{

                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_privacyPolicyFragment)
            }

                              }
            , 3000)

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