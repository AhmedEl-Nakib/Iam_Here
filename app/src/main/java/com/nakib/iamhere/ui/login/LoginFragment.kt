package com.nakib.iamhere.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.nakib.iamhere.R
import com.nakib.iamhere.databinding.LoginFragmentBinding
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    lateinit var binding : LoginFragmentBinding
    private val viewModel: LoginViewModel by inject()
    var locationManager: LocationManager? = null
    var GpsStatus = false
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LoginFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleDrawOverlayApps()
        handleObserver()
        sharedPreferences = requireActivity().getSharedPreferences("iamhere", Context.MODE_PRIVATE)
        if(sharedPreferences.getString("UserId","")!!.isNotEmpty()){
            if(sharedPreferences.getString("UserId","")!! == "AdminId"){
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_adminFragment)
            }else {
                val bundle = bundleOf(
                    "ViewType" to "Normal",
                    "DrName" to sharedPreferences.getString("DrName", ""),
                    "UserId" to sharedPreferences.getString("UserId", "")
                )
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_loginFragment_to_homeNormalFragment, bundle)
            }
        }


        binding.loginBtnId.setOnClickListener {
            if(CheckGpsStatus(requireContext()))
                viewModel.getUserData(requireContext())
            else
                Toast.makeText(requireContext(),"You need to open GPS first !",Toast.LENGTH_SHORT).show()

        }
    }

    private fun handleDrawOverlayApps() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireContext())) {
                val intent =
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${requireActivity().packageName}")
                    )
                startActivityForResult(intent, 0)
            }
        }

    }

    private fun handleObserver() {
        viewModel.userData.observe(viewLifecycleOwner,{
            if(it.userId!!.isNotEmpty()){
                if(it.IsAdmin!! == "0") {
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("UserId", it.userId)
                    editor.putString("DrName", it.DrName)
                    editor.apply()
                    val bundle = bundleOf(
                        "ViewType" to "Normal",
                        "DrName" to it.DrName,
                        "UserId" to it.userId
                    )
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment_to_homeNormalFragment, bundle)
                }else{
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("UserId", "AdminId")
                    editor.apply()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment_to_adminFragment)
                }
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