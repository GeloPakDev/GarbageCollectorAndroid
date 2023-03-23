package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.garbagecollector.databinding.PostLocationBinding
import com.example.garbagecollector.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*


class PostLocationFragment(bitmap: Bitmap) : BottomSheetDialogFragment() {

    private lateinit var binding: PostLocationBinding

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
        binding = PostLocationBinding.inflate(inflater)
        binding.detailGarbagePhoto.setImageBitmap(garbagePhoto)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentGarbageLocation()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getCurrentGarbageLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            //Get Location object
            val location = it.result
            //using Rx Java
            getAddress(location)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ address ->
                    address.getAddressLine(0)
                    val city: String = address.locality ?: ""
                    val postalCode: String = address.postalCode ?: ""
                    val featureName: String = address.featureName ?: ""
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        binding.locationEditText.setText("$featureName, $city $postalCode")
                        binding.postButton.setOnClickListener {
                            saveLocation(latLng, address, garbagePhoto)
                        }
                    }
                },
                    {
                        Log.e("PostLocationFragment", it.message.toString())
                    }
                )
        }
    }


    private fun getAddress(location: Location?): Single<Address> {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses =
            location?.let { geocoder.getFromLocation(it.latitude, location.longitude, 1) }
        //Get Address
        val address = addresses?.get(0)
        return Single.create { subscriber ->
            if (address != null) {
                subscriber.onSuccess(address)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
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