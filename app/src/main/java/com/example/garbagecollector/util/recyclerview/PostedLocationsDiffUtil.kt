package com.example.garbagecollector.util.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.example.garbagecollector.repository.database.model.PostedLocation

class PostedLocationsDiffUtil(
    private val oldList: List<PostedLocation>,
    private val newList: List<PostedLocation>
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