package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.ProfileBinding
import com.example.garbagecollector.util.findTopNavController
import com.example.garbagecollector.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile) {
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!

    private val statistics = listOf("My Garbage", "Ranking")
    private val settings = listOf("Change Language", "Notifications")
    private val others = listOf("About App", "Sign Out")
    private var statisticsListViewAdapter: ArrayAdapter<String>? = null
    private var settingsListViewAdapter: ArrayAdapter<String>? = null
    private var othersListViewAdapter: ArrayAdapter<String>? = null

    private val profileViewModel by viewModels<ProfileViewModel>()

    private val firebaseAuth = FirebaseAuth.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ProfileBinding.inflate(inflater)
        //Check if user signed in already
        if (firebaseAuth.currentUser == null) {
            findNavController().navigate(R.id.notSignedInFragment)
        } else {
            binding.fullName.text = "${firebaseAuth.currentUser?.displayName}"
        }
        //ListView setup
        val statisticsListView = binding.listViewStatistics
        val settingsListView = binding.listViewSettings
        val othersListView = binding.listViewOthers

        statisticsListViewAdapter =
            activity?.let { ArrayAdapter(it, R.layout.list_view_item, statistics) }

        settingsListViewAdapter =
            activity?.let { ArrayAdapter(it, R.layout.list_view_item, settings) }

        othersListViewAdapter =
            activity?.let { ArrayAdapter(it, R.layout.list_view_item, others) }

        statisticsListView.adapter = statisticsListViewAdapter
        settingsListView.adapter = settingsListViewAdapter
        othersListView.adapter = othersListViewAdapter

        statisticsListView.setOnItemClickListener { parent, view, position, id ->
            setStatisticsLitItemListener(position)
        }

//        settingsListView.setOnItemClickListener { parent, view, position, id ->
//            setSettingsLitItemListener(position)
//        }

        othersListView.setOnItemClickListener { parent, view, position, id ->
            setOthersLitItemListener(position)
        }

        binding.edit.setOnClickListener {
            val intent = Intent(activity, ProfileEditActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }

    private fun setStatisticsLitItemListener(listItemPosition: Int) {
        when (statisticsListViewAdapter?.getItem(listItemPosition).toString()) {
            "My Garbage" -> {
                findTopNavController().navigate(R.id.myGarbageFragment)
            }
            "Ranking" -> {
                findTopNavController().navigate(R.id.myRankingFragment)
            }
        }
    }

    private fun setSettingsLitItemListener(listItemPosition: Int) {
        when (settingsListViewAdapter?.getItem(listItemPosition).toString()) {
            "Change Language" -> {}
            "Notifications" -> {}
        }
    }

    private fun setOthersLitItemListener(listItemPosition: Int) {
        when (othersListViewAdapter?.getItem(listItemPosition).toString()) {
            "About App" -> {
                findTopNavController().navigate(R.id.signUpFragment)
            }
            "Sign Out" -> {
                showSuccessDialog()
            }
        }
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.sign_out_dialog)
        val okButton = dialog.findViewById<AppCompatButton>(R.id.ok_button)
        okButton.setOnClickListener {
            firebaseAuth.signOut()
            dialog.dismiss()
        }
        val cancelButton = dialog.findViewById<AppCompatButton>(R.id.cancel_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}