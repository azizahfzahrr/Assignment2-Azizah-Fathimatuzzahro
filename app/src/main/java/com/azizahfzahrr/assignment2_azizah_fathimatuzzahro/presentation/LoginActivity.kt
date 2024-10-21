package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.DiaryPreferenceDataStore
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.UserDao
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.UserDatabase
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val userDao: UserDao by lazy{
        UserDatabase.getDatabase(application).userDao()
    }
    private val dataStore: DiaryPreferenceDataStore by lazy {
        DiaryPreferenceDataStore.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            loginUser()
        }
        binding.tvLoginPromptHighlightLogin.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = binding.etEmailLogin.text.toString().trim()
        val password = binding.passwordEditTextLogin.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Masukkan email & password terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO){
                userDao.getUserByEmail(email)
            }
            if (user != null && user.password == password){
                Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()

                saveUserLoginStatus(true)
                saveLoggedInEmail(user.email)

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Email atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveLoggedInEmail(email: String) {
        lifecycleScope.launch {
            dataStore.setLoggedInEmail(email)
        }
    }

    private fun saveUserLoginStatus(isLoggedIn: Boolean) {
        lifecycleScope.launch {
            dataStore.setUserLoginStatus(isLoggedIn)
        }
    }
}