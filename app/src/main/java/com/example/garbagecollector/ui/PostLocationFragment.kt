package com.example.garbagecollector.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.garbagecollector.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class PostLocationFragment(bitmap: Bitmap) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding
    private val garbagePhoto: Bitmap = bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBinding.inflate(inflater)
        binding.postGarbagePhoto.setImageBitmap(garbagePhoto)
        return binding.root
    }
}