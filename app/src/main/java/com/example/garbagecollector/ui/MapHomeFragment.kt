package com.example.garbagecollector.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.garbagecollector.R
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.repository.web.dto.LocationResponseDto
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.util.findTopNavController
import com.example.garbagecollector.viewmodel.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapHomeFragment : Fragment(R.layout.fragment_map_home), OnMapReadyCallback {
    //To control and query the map
    private lateinit var googleMap: GoogleMap

    //To get current User's location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //ViewModel
    private val locationsViewModel by viewModels<LocationViewModel>()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCurrentLocation()
            } else {
                Log.e(Constants.HOME_FRAGMENT_TAG, "Location permission denied")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        //set up the map and create Google Map object
        mapFragment.getMapAsync(this)
        setupLocationClient()
    }

    //Initialization methods
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        //Get the User's location
        getCurrentLocation()
        //Observe for Location changes
        requestApiData()
        //Listener for Markers on the map
        showDetailLocation(googleMap)
    }

    private fun setupLocationClient() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    //Location related methods
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        //Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //If the permission has not been granted, then requestLocationPermissions() is called.
            startLocationPermissionRequest()
        } else {
            googleMap.isMyLocationEnabled = true
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    //Zoom the user to this location
                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
                    googleMap.moveCamera(update)
                } else {
                    Log.e(Constants.HOME_FRAGMENT_TAG, "No location found!")
                }
            }
        }
    }

    // Ex. Launching ACCESS_FINE_LOCATION permission.
    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
    }

    private fun displayAllMarkers(markers: List<LocationResponseDto>?) {
        viewLifecycleOwner.lifecycleScope.launch {
            //Locate all new markers on the map
            markers?.forEach {
                val latLng = LatLng(it.latitude, it.longitude)
                val marker = googleMap.addMarker(MarkerOptions().position(latLng))
                marker?.tag = it.id
            }
        }
    }

    private fun requestApiData() {
        //Observe for the markers from the Server
        locationsViewModel.getAllLiveLocations()
        locationsViewModel.remoteLocations.observe(viewLifecycleOwner) { response ->
            //Clear all existing markers from the map before retrieving new one
            googleMap.clear()
            when (response) {
                is NetworkResult.Success -> {
                    Log.d("requestApiData: ", response.data.toString())
                    //add method to show dismiss the dialog of loading the icons
                    displayAllMarkers(response.data)
                }
                is NetworkResult.Error -> {
                    if (response.message.toString() == "No Internet Connection.") {
                        showNetworkIssueDialog()
                    }
                }
                is NetworkResult.Loading -> {
                    //show the dialog of uploading the locations
                }
            }
        }
    }

    private fun showDetailLocation(googleMap: GoogleMap) {
        viewLifecycleOwner.lifecycleScope.launch {
            googleMap.setOnMarkerClickListener {
                findTopNavController().navigate(
                    R.id.action_homeFragment_to_detailLocationFragment,
                    bundleOf(Constants.MARKER_ID to it.tag)
                )
                false
            }
        }
    }

    private fun showNetworkIssueDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.network_issues_dialog)
        val okButton = dialog.findViewById<AppCompatButton>(R.id.ok_button)
        okButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun setupSnackBar() {
        val snackBar = Snackbar.make(requireView(), "", Snackbar.LENGTH_INDEFINITE)
        val customSnackView: View = layoutInflater.inflate(R.layout.locations_snackbar, null)
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

        val reload: Button = customSnackView.findViewById(R.id.reload_button)

        snackBar.anchorView =
            requireActivity().findViewById(R.id.bottomNavigationView)

        reload.setOnClickListener {
            requestApiData()
            snackBar.dismiss()
        }

        snackBarLayout.addView(customSnackView, 0)
        snackBar.show()
    }

    private fun clearMap() {
        googleMap.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearMap()
    }
}