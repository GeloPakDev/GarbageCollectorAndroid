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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.ProfileBinding
import com.example.garbagecollector.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!

    private val statistics = listOf("My Garbage", "Ranking")
    private val settings = listOf("Change Language", "Notifications")
    private val others = listOf("About App", "Sign Out")
    private var statisticsListViewAdapter: ArrayAdapter<String>? = null
    private var settingsListViewAdapter: ArrayAdapter<String>? = null
    private var othersListViewAdapter: ArrayAdapter<String>? = null

    private val profileViewModel by viewModels<ProfileViewModel>()


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Check if user signed in already
        profileViewModel.token.observe(viewLifecycleOwner) { token ->
            //If token is empty user is not signed in or registered yet
            if (token.isEmpty()) {
                findNavController().navigate(R.id.notSignedInFragment)
            } else {
                //Get the email from the DataStore and find user by email
                profileViewModel.email.observe(viewLifecycleOwner) { email ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        val user = profileViewModel.findUserByEmail(email)
                        if (user.isSuccessful) {
                            binding.fullName.text =
                                "${user.body()?.data?.firstName} ${user.body()?.data?.lastName}"
                        }
                    }
                }
            }
        }

        _binding = ProfileBinding.inflate(inflater)

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

        settingsListView.setOnItemClickListener { parent, view, position, id ->
            setSettingsLitItemListener(position)
        }

        othersListView.setOnItemClickListener { parent, view, position, id ->
            setOthersLitItemListener(position)
        }

        return binding.root

    }

    private fun setStatisticsLitItemListener(listItemPosition: Int) {
        when (statisticsListViewAdapter?.getItem(listItemPosition).toString()) {
            "My Garbage" -> {
                val intent = Intent(activity, MyGarbageActivity::class.java)
                startActivity(intent)
            }
            "Ranking" -> {
                val intent = Intent(activity, RankingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setSettingsLitItemListener(listItemPosition: Int) {
        when (settingsListViewAdapter?.getItem(listItemPosition).toString()) {
            "Change Language" -> {
                val intent = Intent(activity, MyGarbageActivity::class.java)
                startActivity(intent)
            }
            "Notifications" -> {
                val intent = Intent(activity, RankingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setOthersLitItemListener(listItemPosition: Int) {
        when (othersListViewAdapter?.getItem(listItemPosition).toString()) {
            "About App" -> {
                val intent = Intent(activity, MyGarbageActivity::class.java)
                startActivity(intent)
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
            profileViewModel.signOut()
            findNavController().navigate(R.id.homeFragment)
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