package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.R
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.DiaryDatabase
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.ActivityDetailDiaryBinding
import kotlinx.coroutines.launch

class DetailDiary : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDiaryBinding
    private lateinit var db: DiaryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DiaryDatabase.getDatabase(this)

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
        binding.saveButton.setOnClickListener {
            onSaveButtonClicked()
        }

        startFadeInAnimation()
    }

    private fun onSaveButtonClicked() {
        val title = binding.titleInputDetail.editText?.text.toString()
        val description = binding.thoughtsInputDetail.editText?.text.toString()

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Title & description can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val diaryId = intent.getIntExtra("id", -1)
        if (diaryId == -1) {
            Toast.makeText(this, "Invalid Diary ID", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val diaryToUpdate = db.diaryDao().getDiaryById(diaryId)
            if (diaryToUpdate != null) {
                diaryToUpdate.title = title
                diaryToUpdate.description = description
                db.diaryDao().updateDiary(diaryToUpdate)
                Toast.makeText(this@DetailDiary, "Diary Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@DetailDiary, "Diary not found", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
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