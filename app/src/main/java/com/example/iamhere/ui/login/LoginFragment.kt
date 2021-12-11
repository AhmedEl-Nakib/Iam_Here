package com.example.iamhere.ui.login

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.iamhere.R
import com.example.iamhere.databinding.LoginFragmentBinding
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    lateinit var binding : LoginFragmentBinding
    private val viewModel: LoginViewModel by inject()
    var locationManager: LocationManager? = null
    var GpsStatus = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LoginFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleObserver()
        binding.loginBtnId.setOnClickListener {
            if(CheckGpsStatus(requireContext()))
                viewModel.getUserData(requireContext())
            else
                Toast.makeText(requireContext(),"You need to open GPS first !",Toast.LENGTH_SHORT).show()
//            if(binding.emailId.text.toString().lowercase() == "admin") {
//                val bundle = bundleOf("ViewType" to "Admin")
//                Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_homeNormalFragment, bundle)
//            }else{
//                val bundle = bundleOf("ViewType" to "Normal")
//                Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_homeNormalFragment, bundle)
//            }
        }
    }

    private fun handleObserver() {
        viewModel.userData.observe(viewLifecycleOwner,{
            if(it.userId!!.isNotEmpty()){
                val bundle = bundleOf("ViewType" to "Normal" , "DrName" to it.DrName , "UserId" to it.userId)
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeNormalFragment, bundle)
            }
        })
    }

    private fun CheckGpsStatus(context: Context) : Boolean {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        assert(locationManager != null)
        GpsStatus = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return GpsStatus
    }

}