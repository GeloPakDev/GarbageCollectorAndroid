package com.example.garbagecollector.util.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.example.garbagecollector.repository.database.model.ClaimedLocation

class ClaimedLocationsDiffUtil(
    private val oldList: List<ClaimedLocation>,
    private val newList: List<ClaimedLocation>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }
}