package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.garbagecollector.databinding.DetailLocationBinding
import com.example.garbagecollector.db.model.Location
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.util.DateFormatter
import com.example.garbagecollector.viewmodel.HomeViewModel
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
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

        val location = marker.tag as Location
        binding.detailGarbagePhoto.setImageBitmap(location.photo)
        binding.locationDetailAddress.text = "${location.name}, ${location.city}"
        binding.locationDetailLatlng.text = "${location.latitude}, ${location.longitude}"
        binding.creationDate.text = DateFormatter.formatDate(location.createDate)
        binding.claim.setOnClickListener {
            location.id?.let { claimLocation(it) }
        }
        binding.copy.setOnClickListener {
            copyToClipboard(binding.locationDetailLatlng)
        }

        return binding.root
    }

    private fun claimLocation(locationId: Long) {
        GlobalScope.launch {
            homeViewModel.claimLocation(locationId)
            dismiss()
        }
    }

    private fun copyToClipboard(textView: TextView) {
        val clipboard: ClipboardManager =
            context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(Constants.LOCATION, textView.text)
        clipboard.setPrimaryClip(data)
    }
}