package com.example.garbagecollector.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.garbagecollector.R
import com.example.garbagecollector.databinding.ActivityProfileEditBinding
import com.example.garbagecollector.repository.web.dto.UpdateUserDto
import com.example.garbagecollector.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileEditActivity : AppCompatActivity() {
    private val profileViewModel by viewModels<ProfileViewModel>()
    private lateinit var binding: ActivityProfileEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData(binding)
        binding.saveButton.setOnClickListener { saveData() }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(binding: ActivityProfileEditBinding) {
        profileViewModel.email.observe(this) { email ->
            lifecycleScope.launch {
                val user = profileViewModel.findUserByEmail(email)
                if (user.isSuccessful) {
                    binding.fullName.text =
                        "${user.body()?.data?.firstName} ${user.body()?.data?.lastName}"
                    binding.editTextTextEmailAddress.setText(user.body()?.data?.email)

                    val cities = resources.getStringArray(R.array.uzbekistan_cities)
                    val cityAdapter =
                        ArrayAdapter(applicationContext, R.layout.list_view_item, cities)
                    binding.cityInput.setAdapter(cityAdapter)

                    val districts = resources.getStringArray(R.array.tashkent_districts)
                    val districtAdapter =
                        ArrayAdapter(applicationContext, R.layout.list_view_item, districts)
                    binding.districtInput.setAdapter(districtAdapter)
                }
            }
        }
    }

    private fun saveData() {
        val email = binding.editTextTextEmailAddress.text.toString()
        val city = binding.cityInput.text.toString()
        val district = binding.district.text.toString()
        val user = UpdateUserDto(email, city, district)
        profileViewModel.updateUser(user)
    }
}