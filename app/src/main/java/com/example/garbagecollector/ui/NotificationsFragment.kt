package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.garbagecollector.databinding.NotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var binding: NotificationsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationsBinding.inflate(inflater)
        return binding.root
    }
}