package com.example.garbagecollector.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.example.garbagecollector.R
import com.example.garbagecollector.model.Location
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment : Fragment(), OnMapReadyCallback {
    //To control and query the map
    private lateinit var googleMap: GoogleMap

    //To get current User's location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val homeViewModel by viewModels<HomeViewModel>()

    //Lifecycle methods
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home, container, false)
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
        createBookmarkObserver()

        googleMap.setOnMarkerClickListener {
            val detailLocationFragment = DetailLocationFragment(it)
            detailLocationFragment.show(childFragmentManager, "TAG")
            false
        }
    }

    private fun setupLocationClient() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    //Location related methods
    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(ACCESS_FINE_LOCATION),
            Constants.REQUEST_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        //Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //If the permission has not been granted, then requestLocationPermissions() is called.
            requestLocationPermissions()
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

    //Handle the user’s response to the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.REQUEST_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Log.e(Constants.HOME_FRAGMENT_TAG, "Location permission denied")
            }
        }
    }

    private fun displayAllMarkers(markers: List<Location>) {
        //Locate all new markers on the map
        markers.forEach {
            val latLng = LatLng(it.latitude, it.longitude)
            val marker = googleMap.addMarker(MarkerOptions().position(latLng))
            marker?.tag = it
        }
    }

    private fun createBookmarkObserver() {
        //Observe for the markers from the Repository
        homeViewModel.getMarkers()?.observe(this) {
            //Clear all existing markers from the map before retrieving new one
            googleMap.clear()
            displayAllMarkers(it)
        }
    }
}