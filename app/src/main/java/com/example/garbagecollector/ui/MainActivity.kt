package com.example.garbagecollector.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.ActivityMainBinding
import com.example.garbagecollector.util.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //As soon as the photo has taken open PostLocationFragment
    private val startForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it != null && it.resultCode == RESULT_OK) {
                val bitmap = it.data?.extras?.get(Constants.GARBAGE_IMAGE_INTENT) as Bitmap
                val bottomSheet = PostLocationFragment(bitmap)
                bottomSheet.show(supportFragmentManager, "TAG")
            }
        }

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
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        //Open Camera
        binding.camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResult.launch(intent)
        }
    }
}