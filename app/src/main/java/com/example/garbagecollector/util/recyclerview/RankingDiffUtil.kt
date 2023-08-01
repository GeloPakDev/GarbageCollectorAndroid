package com.example.garbagecollector.util.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.example.garbagecollector.model.User

class RankingDiffUtil(
    private val oldList: List<User>,
    private val newList: List<User>
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