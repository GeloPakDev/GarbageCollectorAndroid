package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.garbagecollector.api.dto.LocationDto
import com.example.garbagecollector.databinding.DetailLocationBinding
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.util.DateFormatter
import com.example.garbagecollector.viewmodel.HomeViewModel
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailLocationFragment(private val marker: Marker) : BottomSheetDialogFragment() {

    private lateinit var binding: DetailLocationBinding

    private val homeViewModel by viewModels<HomeViewModel>()


    @SuppressLint("SetTextI18n", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailLocationBinding.inflate(inflater)

        val location = marker.tag as LocationDto
        //Decode the image to byteArray
        val byteArray = Base64.decode(location.photo, Base64.DEFAULT)
        //Decode the byteArray to Bitmap
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        binding.detailGarbagePhoto.setImageBitmap(bitmap)
        binding.locationDetailAddress.text = "${location.name}, ${location.city}"
        binding.locationDetailLatlng.text = "${location.latitude}, ${location.longitude}"
        //Format the date
        binding.creationDate.text = DateFormatter.convertDateFormat(location.creationDate.toString())
        binding.claim.setOnClickListener {
            location.id?.let { claimLocation(it) }
        }
        binding.copy.setOnClickListener {
            copyToClipboard(binding.locationDetailLatlng)
        }

        return binding.root
    }

    private fun claimLocation(locationId: Long) {
        homeViewModel.token.observe(viewLifecycleOwner) {
            homeViewModel.userId.observe(viewLifecycleOwner) { userId ->
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    homeViewModel.claimLocation(locationId, userId)
                    dismiss()
                }
            }
        }
    }

    private fun copyToClipboard(textView: TextView) {
        val clipboard: ClipboardManager =
            context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(Constants.LOCATION, textView.text)
        clipboard.setPrimaryClip(data)
    }
}