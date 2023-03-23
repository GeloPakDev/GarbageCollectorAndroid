package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garbagecollector.adapters.ClaimedGarbageListAdapter
import com.example.garbagecollector.databinding.FragmentClaimedGarbageBinding
import com.example.garbagecollector.viewmodel.HomeViewModel

class ClaimedGarbageFragment : Fragment() {
    private lateinit var binding: FragmentClaimedGarbageBinding
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClaimedGarbageBinding.inflate(inflater)

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)

        //TODO:Search for better solution in terms of initialization , and clean code
        homeViewModel.getMarkers()?.observe(viewLifecycleOwner) {
            val claimedGarbageListAdapter = ClaimedGarbageListAdapter(it)
            recyclerView.adapter = claimedGarbageListAdapter
        }

        return binding.root
    }
}