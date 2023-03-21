package com.example.garbagecollector.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.ActivityMainBinding
import com.example.garbagecollector.util.Constants
import com.example.garbagecollector.util.Constants.Companion.REQUEST_CAMERA

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Set up the BottomNavigationView
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.background = null
        //Hide the middle button in the bottom app bar
        bottomNavigationView.menu.getItem(2).isEnabled = false
        //Connect Navigation Graph
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        //Open Camera
        binding.camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    //As soon as the photo has taken open PostLocationFragment
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get(Constants.GARBAGE_IMAGE_INTENT) as Bitmap
            val bottomSheet = PostLocationFragment(bitmap)
            bottomSheet.show(supportFragmentManager, "TAG")
        }
    }
}