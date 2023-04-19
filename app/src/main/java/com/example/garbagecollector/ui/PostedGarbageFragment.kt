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
import com.example.garbagecollector.adapters.PostedGarbageListAdapter
import com.example.garbagecollector.databinding.FragmentPostedGarbageBinding
import com.example.garbagecollector.mapper.LocationsMapper
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.viewmodel.PostedGarbageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostedGarbageFragment : Fragment() {

    private var _binding: FragmentPostedGarbageBinding? = null
    private val binding get() = _binding!!
    private val postedGarbageViewModel by viewModels<PostedGarbageViewModel>()
    private val postedGarbageListAdapter by lazy { PostedGarbageListAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostedGarbageBinding.inflate(inflater)
        setupRecyclerView()
        readDatabase()
        return binding.root
    }

    private fun readDatabase() {
        viewLifecycleOwner.lifecycleScope.launch {
            postedGarbageViewModel.postedLocalLocations.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    postedGarbageListAdapter.setData(it)
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