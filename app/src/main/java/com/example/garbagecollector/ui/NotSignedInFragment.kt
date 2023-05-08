package com.example.garbagecollector.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.garbagecollector.databinding.NotSignedInBinding


class NotSignedInFragment : Fragment() {
    private var _binding: NotSignedInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotSignedInBinding.inflate(inflater)
        setUpSignInLink()
        return binding.root
    }

    private fun setUpSignInLink() {
        val text = "You are not logged in. Sign In"
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
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