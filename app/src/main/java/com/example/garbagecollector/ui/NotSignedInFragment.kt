package com.example.garbagecollector.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.NotSignedInBinding
import com.example.garbagecollector.util.findTopNavController

class NotSignedInFragment : Fragment(R.layout.not_signed_in) {
    private var _binding: NotSignedInBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = NotSignedInBinding.bind(view)
        setUpSignInLink()
    }

    private fun setUpSignInLink() {
        val text = "You are not logged in. Sign In"
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findTopNavController().navigate(R.id.signInFragment)
            }
        }
        spannableString.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.notSignIn.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.notSignIn.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}