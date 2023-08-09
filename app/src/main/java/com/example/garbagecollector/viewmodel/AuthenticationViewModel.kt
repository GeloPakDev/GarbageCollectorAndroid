package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.garbagecollector.repository.web.AuthRepository
import com.example.garbagecollector.repository.web.SignUpCallback
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    application: Application
) : AndroidViewModel(application) {

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: SignUpCallback
    ): Task<AuthResult> {
        return authRepository.signUp(firstName, lastName, email, password, callback)
    }

    fun signIn(email: String, password: String): Task<AuthResult> {
        return authRepository.signIn(email, password)
    }
}