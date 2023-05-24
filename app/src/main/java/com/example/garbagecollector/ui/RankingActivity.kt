package com.example.garbagecollector.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.garbagecollector.databinding.ActivityMyRankingBinding
import com.example.garbagecollector.viewmodel.MyRankingViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.eazegraph.lib.models.PieModel


@AndroidEntryPoint
class RankingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyRankingBinding
    private val myRankingViewModel by viewModels<MyRankingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myRankingViewModel.getData()
        setStatisticsData()
        setUpPieChart()
    }

    private fun setUpPieChart() {
        val pieChart = binding.piechart
        myRankingViewModel.statistics.observe(this) {
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
        myRankingViewModel.statistics.observe(this) { statistics ->
            binding.userAllPoints.text = statistics["totalPoints"].toString()
            binding.claimedLocations.text = statistics["claimedLocations"].toString()
            binding.postedLocations.text = statistics["postedLocations"].toString()
            binding.durationPoints.text = statistics["totalPoints"].toString()
        }
    }
}