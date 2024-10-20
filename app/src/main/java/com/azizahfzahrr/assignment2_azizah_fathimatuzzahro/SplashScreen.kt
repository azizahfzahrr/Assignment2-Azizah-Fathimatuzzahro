package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var dataStore: DiaryPreferenceDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStore = DiaryPreferenceDataStore.getInstance(this)

        lifecycleScope.launch {
            val isLoggedIn = dataStore.getUserLoginStatus()
            delay(2000)

            if (isLoggedIn){
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashScreen, SignupActivity::class.java))
                finish()
            }
        }
    }
}
