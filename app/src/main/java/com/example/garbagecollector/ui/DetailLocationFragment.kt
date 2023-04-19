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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.DetailLocationBinding
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.util.DateFormatter
import com.example.garbagecollector.viewmodel.HomeViewModel
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailLocationFragment(private val marker: Marker) : BottomSheetDialogFragment() {

    private lateinit var binding: DetailLocationBinding
    private val homeViewModel by viewModels<HomeViewModel>()


    @SuppressLint("SetTextI18n", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailLocationBinding.inflate(inflater)

        val locationId = marker.tag as Long
        setViewDetails(locationId, binding)

        binding.copy.setOnClickListener {
            copyToClipboard(binding.locationDetailLatlng)
        }

        return binding.root
    }

    @SuppressLint("NewApi", "SetTextI18n")
    private fun setViewDetails(locationId: Long, binding: DetailLocationBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            val locationDto = homeViewModel.getLocationById(locationId)
            Log.d("OBJECT", locationDto.toString())
            //Decode the image to byteArray
            val byteArray = Base64.decode(locationDto.data?.photo, Base64.DEFAULT)
            //Decode the byteArray to Bitmap
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.detailGarbagePhoto.setImageBitmap(bitmap)
            binding.locationDetailAddress.text =
                "${locationDto.data?.name}, ${locationDto.data?.city}"
            binding.locationDetailLatlng.text =
                "${locationDto.data?.latitude}, ${locationDto.data?.longitude}"
            //Format the date
            binding.creationDate.text =
                DateFormatter.convertDateFormat(locationDto.data?.creationDate.toString())
            binding.claim.setOnClickListener {
                locationDto.data?.id?.let { claimLocation(it) }
            }
        }
    }

    private fun claimLocation(locationId: Long) {
        homeViewModel.token.observe(viewLifecycleOwner) {
            homeViewModel.userId.observe(viewLifecycleOwner) { userId ->
                val job = viewLifecycleOwner.lifecycleScope.launch {
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
}