package com.example.garbagecollector.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.garbagecollector.databinding.NotSignedInBinding


class NotSignedInFragment : Fragment() {
    private lateinit var binding: NotSignedInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotSignedInBinding.inflate(inflater)

        binding.signIn.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}