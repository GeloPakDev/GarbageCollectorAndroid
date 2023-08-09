package com.example.garbagecollector.repository.web.impl

import com.example.garbagecollector.repository.web.SignUpCallback
import com.example.garbagecollector.repository.web.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AuthRepository {

    override fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: SignUpCallback
    ): Task<AuthResult> {
        //Register the User in the AuthenticationDatabase
        return auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = auth.currentUser
                //Prepare the data for the user to save
                val userId = firebaseUser!!.uid
                val user = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "photo" to null,
                    "city" to "",
                    "district" to "",
                    "points" to 0,
                    "ranking" to ""
                )
                //Store the User in the FireStore
                firestore.collection("users").document(userId).set(user).addOnCompleteListener {
                    callback.onSignUpComplete(true)
                }
            } else {
                callback.onSignUpComplete(false)
            }
        }
    }

    override fun signIn(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }
}