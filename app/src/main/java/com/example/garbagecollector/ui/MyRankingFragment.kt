package com.example.garbagecollector.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.FragmentMyRankingBinding
import com.example.garbagecollector.viewmodel.MyRankingViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.eazegraph.lib.models.PieModel

@AndroidEntryPoint
class MyRankingFragment : Fragment(R.layout.fragment_my_ranking) {

    private var _binding: FragmentMyRankingBinding? = null
    private val binding get() = _binding!!
    private val myRankingViewModel by viewModels<MyRankingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyRankingBinding.bind(view)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        myRankingViewModel.getData()
        setStatisticsData()
        setUpPieChart()
    }


    private fun setUpPieChart() {
        val pieChart = binding.piechart
        myRankingViewModel.statistics.observe(viewLifecycleOwner) {
            pieChart.addPieSlice(
                it["totalPoints"]?.toFloat()?.let { it1 ->
                    PieModel(
                        "Points", it1 / 10,
                        Color.parseColor("#4DFFDF")
                    )
                }
            )
            pieChart.addPieSlice(
                it["claimedLocations"]?.toFloat()?.let { it1 ->
                    PieModel(
                        "Posted", it1,
                        Color.parseColor("#5B22FF")
                    )
                }
            )
            pieChart.addPieSlice(
                it["postedLocations"]?.toFloat()?.let { it1 ->
                    PieModel(
                        "Claimed", it1,
                        Color.parseColor("#FFD422")
                    )
                }
            )
        }
    }

    private fun setStatisticsData() {
        myRankingViewModel.statistics.observe(viewLifecycleOwner) { statistics ->
            binding.userAllPoints.text = statistics["totalPoints"].toString()
            binding.claimedLocations.text = statistics["claimedLocations"].toString()
            binding.postedLocations.text = statistics["postedLocations"].toString()
            binding.durationPoints.text = statistics["totalPoints"].toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}