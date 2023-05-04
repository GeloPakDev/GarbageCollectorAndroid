package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garbagecollector.adapters.ClaimedGarbageListAdapter
import com.example.garbagecollector.databinding.FragmentClaimedGarbageBinding
import com.example.garbagecollector.mapper.LocationsMapper
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.viewmodel.ClaimedGarbageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClaimedGarbageFragment : Fragment() {

    private var _binding: FragmentClaimedGarbageBinding? = null
    private val binding get() = _binding!!
    private val claimedGarbageViewModel by viewModels<ClaimedGarbageViewModel>()
    private val claimedGarbageListAdapter by lazy { ClaimedGarbageListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClaimedGarbageBinding.inflate(inflater)
        setupRecyclerView()
        readDatabase()
        return binding.root
    }

    private fun readDatabase() {
        viewLifecycleOwner.lifecycleScope.launch {
            //Send request to check if it there is new data available
            val locationsNumber = claimedGarbageViewModel.getTotalLocations()
            claimedGarbageViewModel.claimedLocalLocations.observe(viewLifecycleOwner) {
                if (locationsNumber > it.size) {
                    requestApiData()
                } else if (it.isNotEmpty()) {
                    claimedGarbageListAdapter.setData(it)
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
        claimedGarbageViewModel.getLocations()
        claimedGarbageViewModel.remoteLocations.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        claimedGarbageListAdapter.setData(
                            LocationsMapper.mapLocationDtoToClaimedLocation(
                                it
                            )
                        )
                    }
                }
                is NetworkResult.Error -> {
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
        viewLifecycleOwner.lifecycleScope.launch {
            claimedGarbageViewModel.claimedLocalLocations.observe(viewLifecycleOwner) { locations ->
                if (locations.isNotEmpty()) {
                    claimedGarbageListAdapter.setData(locations)
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
        binding.recyclerview.adapter = claimedGarbageListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}