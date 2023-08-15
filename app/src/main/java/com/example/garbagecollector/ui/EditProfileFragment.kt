package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.FragmentEditProfileBinding
import com.example.garbagecollector.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        val user = viewModel.getAuthInstance().currentUser
        viewLifecycleOwner.lifecycleScope.launch {
            val userDetails = viewModel.getUserDetails(user!!.uid)
            userDetails?.let {
                binding.fullName.text = "${it.firstName} ${it.lastName}"
                binding.editTextEmail.setText(user.email)
                binding.editTextCity.setText(it.city)
                binding.editTextDistrict.setText(it.district)
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signUp.setOnClickListener {
            val job = viewLifecycleOwner.lifecycleScope.launch {
                viewModel.updateUserDetails(
                    user!!.uid,
                    binding.editTextEmail.text.toString(),
                    binding.editTextCity.text.toString(),
                    binding.editTextDistrict.text.toString()
                )
            }
            job.invokeOnCompletion {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}