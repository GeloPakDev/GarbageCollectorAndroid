package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.PostLocationBinding
import com.example.garbagecollector.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
        //If user open camera from different fragment navigate him to the homeFragment
        findNavController().navigate(R.id.homeFragment)
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
            viewLifecycleOwner.lifecycleScope.launch {
                val address = getAddress(location)

                address?.getAddressLine(0)

                val city: String = address?.locality ?: ""
                val postalCode: String = address?.postalCode ?: ""
                val featureName: String = address?.featureName ?: ""
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    binding.locationEditText.setText("$featureName, $city $postalCode")
                    binding.postButton.setOnClickListener {
                        saveLocation(latLng, address, garbagePhoto)
                    }
                }
            }
        }
    }


    private suspend fun getAddress(location: Location?): Address? =
        withContext(Dispatchers.IO) {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses =
                location?.let { geocoder.getFromLocation(it.latitude, location.longitude, 1) }
            //Get Address
            addresses?.get(0)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveLocation(location: LatLng, address: Address?, garbagePhoto: Bitmap) {
        homeViewModel.token.observe(viewLifecycleOwner) {
            homeViewModel.userId.observe(viewLifecycleOwner) {
                val job = viewLifecycleOwner.lifecycleScope.launch {
                    homeViewModel.addLocation(location, address, garbagePhoto)
                }
                job.invokeOnCompletion {
                    dismiss()
                    showSuccessDialog()
                }
            }
        }
    }

    private fun setupLocationClient() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.success_post_dialog)
        val okButton = dialog.findViewById<AppCompatButton>(R.id.ok_button)
        okButton.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }
}