package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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