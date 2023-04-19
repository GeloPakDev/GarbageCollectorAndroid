package com.example.garbagecollector.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.garbagecollector.databinding.PostedGarbageListItemBinding
import com.example.garbagecollector.repository.database.model.PostedLocation
import com.example.garbagecollector.util.PostedLocationsDiffUtil

class PostedGarbageListAdapter : RecyclerView.Adapter<PostedGarbageListAdapter.ViewHolder>() {

    private var locationData = emptyList<PostedLocation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locationItem = locationData[position]
        holder.bind(locationItem)
    }

    class ViewHolder(private val binding: PostedGarbageListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(locationDto: PostedLocation) {
            binding.postedLocation = locationDto
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PostedGarbageListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return locationData.size
    }

    fun setData(list: List<PostedLocation>) {
        val locationsDiffUtil = PostedLocationsDiffUtil(locationData, list)
        val diffUtilResult = DiffUtil.calculateDiff(locationsDiffUtil)
        locationData = list
        diffUtilResult.dispatchUpdatesTo(this)
    }
}