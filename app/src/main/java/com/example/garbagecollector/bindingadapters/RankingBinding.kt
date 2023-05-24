package com.example.garbagecollector.bindingadapters

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.garbagecollector.model.User

class RankingBinding {
    companion object {
        @SuppressLint("SetTextI18n")
        @BindingAdapter("setFullName")
        @JvmStatic
        fun setFullName(textView: TextView, user: User) {
            textView.text = user.firstName + user.lastName
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setUserAddress")
        @JvmStatic
        fun setUserAddress(textView: TextView, user: User) {
            textView.text = "${user.city}, ${user.district}"
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setUserPoints")
        @JvmStatic
        fun setUserPoints(textView: TextView, points: Long) {
            textView.text = points.toString()
        }
    }
}