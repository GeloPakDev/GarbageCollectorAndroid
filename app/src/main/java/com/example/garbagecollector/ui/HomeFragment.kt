package com.example.garbagecollector.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.HomeBinding
import com.example.garbagecollector.util.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home) {
    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!

    //As soon as the photo has taken open PostLocationFragment
    private val startForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it != null && it.resultCode == AppCompatActivity.RESULT_OK) {
                val bitmap = it.data?.extras?.get(Constants.GARBAGE_IMAGE_INTENT) as Bitmap
                val bottomSheet = PostLocationFragment(bitmap)
                bottomSheet.show(childFragmentManager, "TAG")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = HomeBinding.bind(view)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_home_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        binding.bottomNavigationView.menu.getItem(2).setOnMenuItemClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResult.launch(intent)
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}