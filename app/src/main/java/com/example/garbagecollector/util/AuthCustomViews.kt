package com.example.garbagecollector.util

import android.annotation.SuppressLint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.EditText
import com.example.garbagecollector.R

@SuppressLint("ClickableViewAccessibility")
fun setHideShowPassword(password: EditText) {
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
}