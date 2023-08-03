package com.example.garbagecollector.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.garbagecollector.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
    }
}