package com.example.garbagecollector.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.ProfileBinding
import com.example.garbagecollector.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: ProfileBinding

    private val commonListNames =
        listOf("My Garbage", "Ranking", "Change Language", "Notifications")
    private val additionalListNames = listOf("FAQ", "About App")
    private var commonListViewAdapter: ArrayAdapter<String>? = null
    private val profileViewModel by viewModels<ProfileViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Check if user signed in already
        profileViewModel.token.observe(viewLifecycleOwner) {
            if (it == "") {
                findNavController().navigate(R.id.notSignedInFragment)
            }
        }

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

        binding.signOut.setOnClickListener {
            profileViewModel.signOut()
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
                val intent = Intent(activity, RegistrationActivity::class.java)
                startActivity(intent)
            }
            "Notifications" -> {
//                val intent = Intent(activity, LoginActivity::class.java)
//                startActivity(intent)
            }
        }
    }
}