package com.example.garbagecollector.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.garbagecollector.api.dto.LoginDto
import com.example.garbagecollector.databinding.LoginBinding
import com.example.garbagecollector.viewmodel.ProfileViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModels by viewModels<ProfileViewModel>()
    private lateinit var binding: LoginBinding

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
    }

    private fun login(email: String, password: String) {
        val loginDto = LoginDto(email, password)
        viewModels.loginUser(loginDto)
    }
}