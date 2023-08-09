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
import com.example.garbagecollector.databinding.FragmentSigninBinding
import com.example.garbagecollector.util.setHideShowPassword
import com.example.garbagecollector.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_signin) {
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSigninBinding.bind(view)

        val email = binding.email
        val password = binding.password
        val signUp = binding.signUp

        setHideShowPassword(password)

        signUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.signIn.setOnClickListener {
            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    val result = authenticationViewModel.signIn(
                        binding.email.text.toString(),
                        binding.password.text.toString()
                    )
                    result.addOnCompleteListener {
                        if (it.isSuccessful) {
                            findNavController().navigate(
                                R.id.action_signInFragment_to_homeFragment,
                                null,
                                NavOptions.Builder()
                                    .setPopUpTo(R.id.signInFragment, true)
                                    .build()
                            )
                        } else {
                            Toast.makeText(requireContext(), "Error! ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}