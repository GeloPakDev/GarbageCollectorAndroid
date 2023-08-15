package com.example.garbagecollector.bindingadapters

import android.annotation.SuppressLint
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.example.garbagecollector.repository.database.model.ClaimedLocation
import com.example.garbagecollector.repository.database.model.PostedLocation
import com.example.garbagecollector.util.DateFormatter
import com.squareup.picasso.Picasso

class MyGarbageBinding {
    companion object {
        @BindingAdapter("setImageFromString")
        @JvmStatic
        fun setImageFromString(imageView: ImageView, image: String) {
            Picasso.get().load(image).into(imageView)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @BindingAdapter("setGarbageLocationDate")
        @JvmStatic
        fun setGarbageLocationDate(textView: TextView, date: String?) {
            if (date.isNullOrBlank()) {
                textView.text = ""
            } else {
                textView.text = DateFormatter.convertDateFormat(date)
            }
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setClaimedLocationAddress")
        @JvmStatic
        fun setClaimedGarbageLocationAddress(textView: TextView, claimedLocation: ClaimedLocation) {
            textView.text =
                "${claimedLocation.name}, ${claimedLocation.city}, ${claimedLocation.postalCode.toString()}"
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setPostedLocationAddress")
        @JvmStatic
        fun setPostedGarbageLocationAddress(textView: TextView, postedLocation: PostedLocation) {
            textView.text =
                "${postedLocation.name}, ${postedLocation.city}, ${postedLocation.postalCode.toString()}"
        }
    }
}