package com.example.garbagecollector.util

import androidx.recyclerview.widget.DiffUtil
import com.example.garbagecollector.repository.web.dto.LocationDto

class LocationsDiffUtil(private val oldList: List<LocationDto>, private val newList: List<LocationDto>) :
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