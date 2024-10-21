package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.R
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(DiaryFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_add -> {
                    ContextCompat.getColorStateList(this, R.color.bottom_navigation_item_color)
                    replaceFragment(DiaryFragment())
                    true
                }

                R.id.nav_settings -> {
                    ContextCompat.getColorStateList(this, R.color.bottom_navigation_item_color)
                    replaceFragment(SettingsFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    companion object {
        const val REQUEST_CODE_ADD_DIARY = 1
    }
}