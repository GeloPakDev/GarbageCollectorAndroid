package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garbagecollector.R
import com.example.garbagecollector.adapters.ClaimedGarbageListAdapter
import com.example.garbagecollector.databinding.FragmentClaimedGarbageBinding
import com.example.garbagecollector.mapper.LocationsMapper
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.viewmodel.ClaimedGarbageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimedGarbageFragment : Fragment(R.layout.fragment_claimed_garbage) {

    private val binding get() = _binding!!
    private var _binding: FragmentClaimedGarbageBinding? = null
    private val claimedGarbageViewModel by viewModels<ClaimedGarbageViewModel>()
    private val claimedGarbageListAdapter by lazy { ClaimedGarbageListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentClaimedGarbageBinding.bind(view)
        setupRecyclerView()
        val userId = claimedGarbageViewModel.getAuthInstance().currentUser?.uid
        if (userId != null) {
            requestApiData(userId)
        }
    }

    private fun requestApiData(userId: String) {
        claimedGarbageViewModel.getAllLiveLocations(userId)
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


    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = claimedGarbageListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}