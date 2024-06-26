package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.DetailLocationBinding
import com.example.garbagecollector.mapper.LocationsMapper
import com.example.garbagecollector.repository.database.model.Location
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.util.DateFormatter
import com.example.garbagecollector.viewmodel.HomeViewModel
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailLocationFragment(private val marker: Marker) : BottomSheetDialogFragment(R.layout.detail_location) {

    private val binding get() = _binding!!
    private var _binding: DetailLocationBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DetailLocationBinding.bind(view)
        val locationId = marker.tag as Long
        readDatabase(locationId, binding)

        binding.copy.setOnClickListener {
            copyToClipboard(binding.locationDetailLatlng)
        }
    }

    private fun readDatabase(locationId: Long, binding: DetailLocationBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            val location = homeViewModel.getLocalLocationById(locationId)
            if (location != null) {
                setViewDetails(location, binding)
            } else {
                val retrievedLocation = homeViewModel.getLocationById(locationId)
                val convertedLocation =
                    LocationsMapper.mapLocationDtoToLocation(retrievedLocation.data)
                homeViewModel.addLocalLocation(convertedLocation)
                setViewDetails(convertedLocation, binding)
            }
        }
    }

    @SuppressLint("NewApi", "SetTextI18n")
    private fun setViewDetails(location: Location, binding: DetailLocationBinding) {
        val byteArray = Base64.decode(location.photo, Base64.DEFAULT)
        //Decode to Bitmap to set it
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        binding.detailGarbagePhoto.setImageBitmap(bitmap)
        binding.locationDetailAddress.text =
            "${location.name}, ${location.city}"
        binding.locationDetailLatlng.text =
            "${location.latitude}, ${location.longitude}"
        //Format the date
        binding.creationDate.text =
            DateFormatter.convertDateFormat(location.createDate.toString())
        binding.claim.setOnClickListener {
            location.id?.let { it1 -> claimLocation(it1) }
        }
    }

    private fun claimLocation(locationId: Long) {
        homeViewModel.token.observe(viewLifecycleOwner) {
            homeViewModel.userId.observe(viewLifecycleOwner) { userId ->
                val job = viewLifecycleOwner.lifecycleScope.launch{
                    homeViewModel.claimLocation(locationId, userId)
                    dismiss()
                }
                job.invokeOnCompletion {
                    dismiss()
                    findNavController().navigate(R.id.homeFragment)
                    showSuccessDialog()
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

    private fun showSuccessDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.success_claim_dialog)
        val okButton = dialog.findViewById<AppCompatButton>(R.id.ok_button)
        okButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}