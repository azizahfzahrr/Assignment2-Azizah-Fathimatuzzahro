package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.R
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.ActivityDetailDiaryBinding

class DetailDiary : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Edit My Diary"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val date = intent.getStringExtra("date")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")

        binding.dateInputDetail.editText?.setText(date)
        binding.dateInputDetail.editText?.apply {
            isEnabled = false
            isClickable = false
        }
        binding.titleInputDetail.editText?.setText(title)
        binding.thoughtsInputDetail.editText?.setText(description)

        startFadeInAnimation()
    }

    private fun startFadeInAnimation() {
        binding.titleInputDetail.alpha = 0f
        binding.dateInputDetail.alpha = 0f
        binding.thoughtsInputDetail.alpha = 0f

        binding.titleInputDetail.animate().alpha(1f).setDuration(500).start()
        binding.dateInputDetail.animate().alpha(1f).setStartDelay(200).setDuration(500).start()
        binding.thoughtsInputDetail.animate().alpha(1f).setStartDelay(400).setDuration(500).start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}