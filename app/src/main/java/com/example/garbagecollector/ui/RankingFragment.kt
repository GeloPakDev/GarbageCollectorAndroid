package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garbagecollector.R
import com.example.garbagecollector.adapters.RankingListAdapter
import com.example.garbagecollector.databinding.RankingBinding
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.viewmodel.RankingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingFragment : Fragment() {

    private var _binding: RankingBinding? = null
    private val binding get() = _binding!!
    private val rankingViewModel by viewModels<RankingViewModel>()
    private val rankingListAdapter by lazy { RankingListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RankingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestApiData()
        setUpRecyclerView()
    }

    private fun requestApiData() {
        rankingViewModel.getAllUsers()
        rankingViewModel.remoteUsers.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        rankingListAdapter.setData(
                            it
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

    private fun setUpRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = rankingListAdapter
        val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(getDrawable(requireContext() , R.drawable.divider)!!)
        binding.recyclerview.addItemDecoration(itemDecoration)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}