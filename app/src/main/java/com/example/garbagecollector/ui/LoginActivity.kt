package com.example.garbagecollector.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import com.example.garbagecollector.R
import com.example.garbagecollector.repository.web.dto.LoginDto
import com.example.garbagecollector.databinding.LoginBinding
import com.example.garbagecollector.viewmodel.ProfileViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    private val viewModels by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.emailEditText
        val password = binding.passwordEditText

        binding.login.setOnClickListener {
            login(email.text.toString(), password.text.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        setSignUpLink()
    }

    private fun login(email: String, password: String) {
        val loginDto = LoginDto(email, password)
        viewModels.loginUser(loginDto)
    }

    private fun setSignUpLink() {
        val text = getString(R.string.sign_up_title)
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
                startActivity(intent)
            }
        }
        spannableString.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signUpLink.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.signUpLink.movementMethod = LinkMovementMethod.getInstance()
    }
}