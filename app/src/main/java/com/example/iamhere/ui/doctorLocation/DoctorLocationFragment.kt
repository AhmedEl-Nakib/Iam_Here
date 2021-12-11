package com.example.iamhere.ui.doctorLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.iamhere.databinding.DoctorLocationFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DoctorLocationFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: DoctorLocationFragmentBinding
    lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DoctorLocationFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapsInitializer.initialize(requireActivity())
        binding.mapview.onCreate(savedInstanceState)
        binding.mapview.getMapAsync(this)
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
        val location = LatLng(30.0151376, 31.3831211)
        map.addMarker(MarkerOptions().position(location))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f))

    }


}