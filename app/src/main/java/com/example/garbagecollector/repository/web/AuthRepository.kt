package com.example.garbagecollector.repository.web

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface AuthRepository {
    fun signIn(email: String, password: String): Task<AuthResult>
    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: SignUpCallback
    ): Task<AuthResult>
}