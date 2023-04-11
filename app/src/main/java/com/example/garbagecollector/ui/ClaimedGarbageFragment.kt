package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garbagecollector.adapters.ClaimedGarbageListAdapter
import com.example.garbagecollector.databinding.FragmentClaimedGarbageBinding
import com.example.garbagecollector.viewmodel.MyGarbageViewModel
import kotlinx.coroutines.launch

class ClaimedGarbageFragment : Fragment() {
    private lateinit var binding: FragmentClaimedGarbageBinding
    private val myGarbageViewModel by viewModels<MyGarbageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClaimedGarbageBinding.inflate(inflater)

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        myGarbageViewModel.token.observe(viewLifecycleOwner) {
            myGarbageViewModel.userId.observe(viewLifecycleOwner) { userId ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val locations = myGarbageViewModel.getClaimedUserLocations(userId)
                    val claimedGarbageListAdapter = ClaimedGarbageListAdapter(locations)
                    recyclerView.adapter = claimedGarbageListAdapter
                }
            }
        }
        return binding.root
    }
}