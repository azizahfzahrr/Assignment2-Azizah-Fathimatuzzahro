package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.ActivitySignupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userDatabase: UserDatabase
    private lateinit var dataStore: DiaryPreferenceDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDatabase = UserDatabase.getDatabase(this)
        dataStore = DiaryPreferenceDataStore.getInstance(this)

        binding.signupButton.setOnClickListener {
            signUpUser()
        }
        binding.tvLoginPromptHighlight.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

        when {
            username.isEmpty() -> {
                Toast.makeText(this, "Mohon masukkan username", Toast.LENGTH_SHORT).show()
                return
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Mohon masukkan email", Toast.LENGTH_SHORT).show()
                return
            }
            password.isEmpty() || confirmPassword.isEmpty() -> {
                Toast.makeText(this, "Mohon masukkan password dan konfirmasi password", Toast.LENGTH_SHORT).show()
                return
            }
            password != confirmPassword -> {
                Toast.makeText(this, "Kata sandi tidak cocok. Mohon periksa kembali", Toast.LENGTH_SHORT).show()
                return
            }
        }

        lifecycleScope.launch {
            val existingUser = getUserByEmail(email)
            if (existingUser != null) {
                Toast.makeText(this@SignupActivity, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val newUser = UserEntity(
                    nama = username,
                    email = email,
                    password = password
                )
                insertUser(newUser)
            }
        }
    }

    private suspend fun getUserByEmail(email: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDatabase.userDao().getUserByEmail(email)
        }
    }

    private suspend fun insertUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            userDatabase.userDao().insertUser(user)
        }
        withContext(Dispatchers.Main) {
            Toast.makeText(this@SignupActivity, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()

            saveUserRegistrationStatus(user.email)

            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveUserRegistrationStatus(email: String) {
        lifecycleScope.launch {
            dataStore.setUserRegistrationStatus(true, email)
        }
    }
}