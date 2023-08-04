package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        val password = binding.password
        val signIn = binding.signIn
        val signUp = binding.signUp
        var passwordVisible = true
        password.setOnTouchListener { _, event ->
            val right = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= password.right - password.compoundDrawables[right].bounds.width()) { // your action here
                    val selection = password.selectionEnd
                    if (passwordVisible) {
                        password.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.closed_eye,
                            0
                        )
                        password.transformationMethod = PasswordTransformationMethod.getInstance()
                        passwordVisible = false
                    } else {
                        password.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.open_eye,
                            0
                        )
                        password.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        passwordVisible = true
                    }
                    password.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
        signIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        signUp.setOnClickListener {
            registerUser(
                binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.email.text.toString(),
                binding.password.text.toString()
            )
        }
    }

    private fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    val userId = firebaseUser!!.uid
                    Toast.makeText(context, "Account created$userId", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)

                    //val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                    //val hashMap = HashMap<String, String>()
                    //hashMap["userId"] = userId
                    //hashMap["firstName"] = firstName
                    //hashMap["lastName"] = lastName
                    //hashMap["email"] = email
                    //databaseReference.setValue(hashMap).addOnCompleteListener { task ->
                    //    if (task.isSuccessful) {
                    //        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                    //    }
                    //}
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}