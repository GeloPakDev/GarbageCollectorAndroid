package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.FragmentSignUpBinding
import com.example.garbagecollector.repository.web.SignUpCallback
import com.example.garbagecollector.util.setHideShowPassword
import com.example.garbagecollector.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        val firstName = binding.firstName
        val lastName = binding.lastName
        val email = binding.email
        val password = binding.password
        val signIn = binding.signIn
        val signUp = binding.signUp

        setHideShowPassword(password)

        signIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        signUp.setOnClickListener {
            if (firstName.text.toString().isEmpty() ||
                lastName.text.toString().isEmpty() ||
                email.text.toString().isEmpty() ||
                password.text.toString().isEmpty()
            ) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.signUp(
                        binding.firstName.text.toString(),
                        binding.lastName.text.toString(),
                        binding.email.text.toString(),
                        binding.password.text.toString(),
                        object : SignUpCallback {
                            override fun onSignUpComplete(successful: Boolean) {
                                if (successful) {
                                    findNavController().navigate(
                                        R.id.action_signUpFragment_to_signInFragment,
                                        null,
                                        NavOptions.Builder()
                                            .setPopUpTo(R.id.signUpFragment, true)
                                            .build()
                                    )
                                } else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}