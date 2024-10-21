package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.presentation

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.DiaryDatabase
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.DiaryEntity
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.ActivityItemDiaryInputBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class ItemDiaryInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemDiaryInputBinding
    private lateinit var db: DiaryDatabase
    private val selectedCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityItemDiaryInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "My Diary"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initializeDatabase()

        setDefaultCurrentDate()

        binding.saveButton.setOnClickListener {
            saveDiaryEntry()
        }

        binding.dateInput.editText?.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setDefaultCurrentDate() {
        val currentYear = selectedCalendar.get(Calendar.YEAR)
        val currentMonth = selectedCalendar.get(Calendar.MONTH) + 1
        val currentDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val formattedDate = "$currentDay/$currentMonth/$currentYear"
        binding.dateInput.editText?.setText(formattedDate)
    }

    private fun initializeDatabase() {
        try {

            db = DiaryDatabase.getDatabase(applicationContext)
        } catch (e: Exception) {

            Log.e("ItemDiaryInputActivity", "Database initialization failed: ${e.message}")
            Toast.makeText(this, "Database initialization error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDiaryEntry() {
        val title = binding.titleInput.editText?.text.toString()
        val date = binding.dateInput.editText?.text.toString()
        val thoughts = binding.thoughtsInput.editText?.text.toString()

        if (title.isEmpty() || date.isEmpty() || thoughts.isEmpty()) {
            Toast.makeText(this, "Isi terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val diaryEntity = DiaryEntity(
            title = title,
            description = thoughts,
            date = selectedCalendar.timeInMillis
        )

        saveDiary(diaryEntity)
    }

    private fun saveDiary(diaryEntity: DiaryEntity) {
        lifecycleScope.launch {
            try {
                db.diaryDao().insertDiary(diaryEntity)
                Toast.makeText(this@ItemDiaryInputActivity, "Your diary is successfully saved", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } catch (e: Exception) {
                Log.e("ItemDiaryInputActivity", "Failed to save diary: ${e.message}")
                Toast.makeText(this@ItemDiaryInputActivity, "Failed to save diary", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                binding.dateInput.editText?.setText(selectedDate)
                selectedCalendar.set(year, monthOfYear, dayOfMonth)
            },
            selectedCalendar.get(Calendar.YEAR),
            selectedCalendar.get(Calendar.MONTH),
            selectedCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
