package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garbagecollector.adapters.PostedGarbageListAdapter
import com.example.garbagecollector.databinding.FragmentPostedGarbageBinding
import com.example.garbagecollector.viewmodel.HomeViewModel

class PostedGarbageFragment : Fragment() {

    private lateinit var binding: FragmentPostedGarbageBinding
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostedGarbageBinding.inflate(inflater)

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)

        //TODO:Search for better solution in terms of initialization , and clean code
        homeViewModel.getMarkers()?.observe(viewLifecycleOwner) {
            val postedGarbageListAdapter = PostedGarbageListAdapter(it)
            recyclerView.adapter = postedGarbageListAdapter
        }

        return binding.root
    }

}