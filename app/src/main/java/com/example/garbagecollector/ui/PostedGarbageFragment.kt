package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garbagecollector.R
import com.example.garbagecollector.adapters.PostedGarbageListAdapter
import com.example.garbagecollector.databinding.FragmentPostedGarbageBinding
import com.example.garbagecollector.mapper.LocationsMapper
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.viewmodel.PostedGarbageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostedGarbageFragment : Fragment(R.layout.fragment_posted_garbage) {

    private var _binding: FragmentPostedGarbageBinding? = null
    private val binding get() = _binding!!
    private val postedGarbageViewModel by viewModels<PostedGarbageViewModel>()
    private val postedGarbageListAdapter by lazy { PostedGarbageListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPostedGarbageBinding.bind(view)
        setupRecyclerView()
        readDatabase()
    }

    private fun readDatabase() {
        viewLifecycleOwner.lifecycleScope.launch {
            //Send request to check if it there is new data available
            val locationsNumber = postedGarbageViewModel.getTotalLocations()
            postedGarbageViewModel.postedLocalLocations.observe(viewLifecycleOwner) {
                if (locationsNumber > it.size) {
                    requestApiData()
                } else if (it.isNotEmpty()) {
                    postedGarbageListAdapter.setData(it)
                } else if (locationsNumber == 0) {
                    //display it on the screen
                    Toast.makeText(
                        requireContext(), "You didn't post any garbage yet",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        postedGarbageViewModel.getLocations()
        postedGarbageViewModel.remoteLocations.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        postedGarbageListAdapter.setData(
                            LocationsMapper.mapLocationDtoToPostedLocation(
                                it
                            )
                        )
                    }
                }
                is NetworkResult.Error -> {
                    //handle case when user didn't post any garbage
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(), response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }


    private fun loadDataFromCache() {
        lifecycleScope.launch {
            postedGarbageViewModel.postedLocalLocations.observe(viewLifecycleOwner) { locations ->
                if (locations.isNotEmpty()) {
                    postedGarbageListAdapter.setData(locations)
                } else {
                    //display it on the screen
                    Toast.makeText(
                        requireContext(), "You didn't post any garbage yet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = postedGarbageListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}