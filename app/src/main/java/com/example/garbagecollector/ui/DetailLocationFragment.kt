package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.DetailLocationBinding
import com.example.garbagecollector.repository.web.dto.LocationResponseDto
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.util.DateFormatter
import com.example.garbagecollector.viewmodel.LocationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailLocationFragment : BottomSheetDialogFragment(R.layout.detail_location) {

    private val binding get() = _binding!!
    private var _binding: DetailLocationBinding? = null
    private val locationsViewModel by viewModels<LocationViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DetailLocationBinding.bind(view)
        val locationId = requireArguments().getString(Constants.MARKER_ID)
        locationId?.let { readDatabase(it, binding) }

        binding.copy.setOnClickListener {
            copyToClipboard(binding.locationDetailLatlng)
        }
    }

    private fun readDatabase(locationId: String, binding: DetailLocationBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            val location = locationsViewModel.getLocationById(locationId)
            setViewDetails(location, binding)
        }
    }

    @SuppressLint("NewApi", "SetTextI18n")
    private fun setViewDetails(location: LocationResponseDto, binding: DetailLocationBinding) {
        val link = location.photo
        val imageView = binding.detailGarbagePhoto
        Picasso.get().load(link).into(imageView)
        binding.locationDetailAddress.text =
            "${location.name}, ${location.city}"
        binding.locationDetailLatlng.text =
            "${location.latitude}, ${location.longitude}"
        //Format the date
        binding.creationDate.text =
            DateFormatter.convertDateFormat(location.creationDate.toString())
        binding.claim.setOnClickListener {
            val locationId = requireArguments().getString(Constants.MARKER_ID)
            Log.d("setViewDetails: ", locationId.toString() + " " + location.id)
            if (locationId != null) {
                claimLocation(locationId)
            }
        }
    }

    private fun claimLocation(id: String) {
        val user = locationsViewModel.getAuthInstance().currentUser
        if (user != null) {
            val job = viewLifecycleOwner.lifecycleScope.launch {
                locationsViewModel.claimLocation(id)
            }
            job.invokeOnCompletion {
                dismiss()
                findNavController().navigate(R.id.homeFragment)
                showSuccessDialog()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "You need to be logged in to claim a location",
                Toast.LENGTH_SHORT
            ).show()
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