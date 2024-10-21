package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.DiaryPreferenceDataStore
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.UserDao
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.UserDatabase
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.ActivityProfileBinding
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userDao: UserDao
    private lateinit var preferenceDataStore: DiaryPreferenceDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = UserDatabase.getDatabase(this)
        userDao = db.userDao()
        preferenceDataStore = DiaryPreferenceDataStore.getInstance(this)

        lifecycleScope.launch {
            val email = preferenceDataStore.getLoggedInEmail()
            if (email != null) {
                val user = userDao.getUserByEmail(email)
                if (user != null) {
                    binding.tvUsernameProfile.text = user.nama
                    binding.tvEmailProfile.text = user.email
                } else {
                    Toast.makeText(this@ProfileActivity, "Profile tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@ProfileActivity, "Email tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}