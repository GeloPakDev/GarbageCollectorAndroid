package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garbagecollector.R
import com.example.garbagecollector.adapters.PostedGarbageListAdapter
import com.example.garbagecollector.databinding.FragmentPostedGarbageBinding
import com.example.garbagecollector.mapper.LocationsMapper
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.viewmodel.PostedGarbageViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        val userId = postedGarbageViewModel.getAuthInstance().currentUser?.uid
        if (userId != null) {
            requestApiData(userId)
        }
    }

    private fun requestApiData(userId: String) {
        postedGarbageViewModel.getAllLiveLocations(userId)
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
//                    loadDataFromCache()
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
        binding.recyclerview.adapter = postedGarbageListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}