package com.nakib.iamhere.ui.doctorLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nakib.iamhere.databinding.DoctorLocationFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.nakib.iamhere.R
import com.nakib.iamhere.ui.admin.AdminViewModel
import com.nakib.iamhere.utils.CommonUtils
import org.koin.android.ext.android.inject

class DoctorLocationFragment : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: DoctorLocationFragmentBinding
    lateinit var map: GoogleMap
    private val viewModel: DoctorLocationViewModel by inject()

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DoctorLocationFragmentBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        MapsInitializer.initialize(requireActivity())
//        binding.mapview.onCreate(savedInstanceState)
//        binding.mapview.getMapAsync(this)
//        binding.arrowBack.setOnClickListener {
//            requireActivity().onBackPressed()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.doctor_location_fragment)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        MapsInitializer.initialize(this)
        binding.mapview.onCreate(savedInstanceState)
        binding.mapview.getMapAsync(this)
        binding.arrowBack.setOnClickListener {
            onBackPressed()
        }
        viewModel.getDoctorLocationList(this,intent.getStringExtra("UserId")!!,intent.getStringExtra("Date")!!)
    }

    override fun onResume() {
        binding.mapview.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapview.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapview.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        handleObserver()
//        val location = LatLng(intent.getStringExtra("lat")!!.toDouble(), intent.getStringExtra("lon")!!.toDouble())
//        map.addMarker(MarkerOptions().position(location))
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f))

    }

    private fun handleObserver() {
        viewModel.doctorLocationData.observe(this,{
            if(it.isNotEmpty()){
                val builder: LatLngBounds.Builder = LatLngBounds.Builder()

                it.forEach { doctorLocationItem ->
                    val location = LatLng(doctorLocationItem.latitude.toDouble(), doctorLocationItem.longitude.toDouble())
                    map.addMarker(MarkerOptions().position(location).title(doctorLocationItem.fromTime))
                    builder.include(location)

                }
                val bounds: LatLngBounds = builder.build()
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 50)
                map.animateCamera(cu)
            }
        })
    }


}