package com.example.garbagecollector.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.ProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: ProfileBinding

    private val commonListNames =
        listOf("My Garbage", "Ranking", "Change Language", "Notifications")
    private val additionalListNames = listOf("FAQ", "About App")
    private var commonListViewAdapter: ArrayAdapter<String>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Check if user signed in already
        binding = ProfileBinding.inflate(inflater)
        //ListView setup
        val commonListView = binding.commonListView
        val additionalListView = binding.additionalListView
        commonListViewAdapter =
            activity?.let { ArrayAdapter(it, R.layout.list_view_item, commonListNames) }

        val additionalListViewAdapter =
            activity?.let { ArrayAdapter(it, R.layout.list_view_item, additionalListNames) }

        commonListView.adapter = commonListViewAdapter
        additionalListView.adapter = additionalListViewAdapter

        commonListView.setOnItemClickListener { parent, view, position, id ->
            setCommonLitItemListener(position)
        }
        return binding.root
    }

    private fun setCommonLitItemListener(listItemPosition: Int) {
        when (commonListViewAdapter?.getItem(listItemPosition).toString()) {
            "My Garbage" -> {
                val intent = Intent(activity, MyGarbageActivity::class.java)
                startActivity(intent)
            }
            "Ranking" -> {

            }
        }
    }
}