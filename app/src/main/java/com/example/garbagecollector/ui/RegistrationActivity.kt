package com.example.garbagecollector.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.garbagecollector.api.dto.RegistrationDto
import com.example.garbagecollector.databinding.ActivityRegistrationBinding
import com.example.garbagecollector.viewmodel.ProfileViewModel

class RegistrationActivity : AppCompatActivity() {

    private val viewModels by viewModels<ProfileViewModel>()
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstName = binding.firstNameEditText
        val lastName = binding.lastNameEditText
        val login = binding.loginEditText
        val password = binding.passwordEditText
        binding.register.setOnClickListener {
            registerUser(
                firstName.text.toString(),
                lastName.text.toString(),
                login.text.toString(),
                password.text.toString()
            )
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val text = "Already Registered? Sign In"
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        spannableString.setSpan(clickableSpan, 20, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signIn.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.signIn.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun registerUser(firstName: String, lastName: String, login: String, password: String) {
        val user = RegistrationDto(firstName, lastName, login, password)
        viewModels.registerUser(user)
    }
}