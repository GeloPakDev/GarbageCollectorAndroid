package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.garbagecollector.databinding.BottomSheetBinding
import com.example.garbagecollector.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class PostLocationFragment(bitmap: Bitmap) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding

    //Get current user's location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val garbagePhoto: Bitmap = bitmap
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLocationClient()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBinding.inflate(inflater)
        binding.postGarbagePhoto.setImageBitmap(garbagePhoto)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentGarbageLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentGarbageLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            //Get Location object
            val location = it.result

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            //Get Address
            val address = addresses?.get(0)
            //Get Data from Address
            address!!.getAddressLine(0)
            val city: String = address.locality
            val postalCode: String = address.postalCode
            val featureName: String = address.featureName

            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                binding.locationEditText.setText("$featureName, $city $postalCode")
                binding.postButton.setOnClickListener {
                    saveLocation(latLng, address, garbagePhoto)
                }
            }
        }
    }

    private fun saveLocation(location: LatLng, address: Address, garbagePhoto: Bitmap) {
        GlobalScope.launch {
            homeViewModel.addLocation(location, address, garbagePhoto)
            dismiss()
        }
    }

    private fun setupLocationClient() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }
}