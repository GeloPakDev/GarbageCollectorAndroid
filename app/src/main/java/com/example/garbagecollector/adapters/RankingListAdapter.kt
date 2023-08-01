package com.example.garbagecollector.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.garbagecollector.databinding.RatingListItemBinding
import com.example.garbagecollector.model.User
import com.example.garbagecollector.util.recyclerview.RankingDiffUtil

class RankingListAdapter : RecyclerView.Adapter<RankingListAdapter.ViewHolder>() {
    private var userList = emptyList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locationItem = userList[position]
        holder.bind(locationItem)
    }

    class ViewHolder(private val binding: RatingListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.user = user
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RatingListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(list: List<User>) {
        val locationsDiffUtil = RankingDiffUtil(userList, list)
        val diffUtilResult = DiffUtil.calculateDiff(locationsDiffUtil)
        userList = list
        diffUtilResult.dispatchUpdatesTo(this)
    }
}