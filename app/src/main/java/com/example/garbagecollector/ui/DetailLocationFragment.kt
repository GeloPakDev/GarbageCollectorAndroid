package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.garbagecollector.databinding.DetailLocationBinding
import com.example.garbagecollector.model.Location
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DetailLocationFragment(private val marker: Marker) : BottomSheetDialogFragment() {

    private lateinit var binding: DetailLocationBinding


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailLocationBinding.inflate(inflater)

        val location: Location = marker.tag as Location
        binding.postGarbagePhoto.setImageBitmap(location.photo)
        binding.locationDetailAddress.text = "${location.name}, ${location.city}"
        binding.locationDetailLatlng.text = "${location.latitude}, ${location.longitude}"

        return binding.root
    }
}