package com.example.garbagecollector.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.garbagecollector.databinding.ClaimedGarbageListItemBinding
import com.example.garbagecollector.repository.database.model.ClaimedLocation
import com.example.garbagecollector.util.recyclerview.ClaimedLocationsDiffUtil

/*
Adapter for RecyclerView in ClaimedGarbageListFragment
 */
class ClaimedGarbageListAdapter : RecyclerView.Adapter<ClaimedGarbageListAdapter.ViewHolder>() {

    private var locationData = emptyList<ClaimedLocation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locationItem = locationData[position]
        holder.bind(locationItem)
    }

    override fun getItemCount(): Int = locationData.size

    class ViewHolder(private val binding: ClaimedGarbageListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(locationDto: ClaimedLocation) {
            binding.claimedLocation = locationDto
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ClaimedGarbageListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    fun setData(list: List<ClaimedLocation>) {
        val locationsDiffUtil = ClaimedLocationsDiffUtil(locationData, list)
        val diffUtilResult = DiffUtil.calculateDiff(locationsDiffUtil)
        locationData = list
        diffUtilResult.dispatchUpdatesTo(this)
    }
}