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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val homeViewModel by viewModels<HomeViewModel>()

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

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        initPlaces()
        //Get the User's location
        getCurrentLocation()
//        googleMap.setOnMarkerClickListener {
//            val intent = Intent(requireContext(), LocationDetailActivity::class.java)
//            intent.putExtra("title", it.title)
//            startActivity(intent)
//            false
//        }
    }

    private fun setupLocationClient() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

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
                    Log.e(Constants.HOME_TAG, "No location found!")
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
                Log.e(Constants.HOME_TAG, "Location permission denied")
            }
        }
    }

    private fun initPlaces() {
        val home = LatLng(41.21630723901391, 69.21264834550048)
        val university = LatLng(41.25843942952858, 69.22009980929104)
        val school = LatLng(41.22161667085518, 69.21318487920138)
        val epam = LatLng(41.29657495340309, 69.27538979238629)

        googleMap.addMarker(MarkerOptions().position(home))
        googleMap.addMarker(MarkerOptions().position(university))
        googleMap.addMarker(MarkerOptions().position(school))
        googleMap.addMarker(MarkerOptions().position(epam))
    }
}