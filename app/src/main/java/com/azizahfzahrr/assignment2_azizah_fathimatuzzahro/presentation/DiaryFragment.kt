package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.presentation

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.R
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.DiaryDatabase
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.DiaryEntity
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.DiaryPreferenceDataStore
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.FragmentDiaryBinding
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.domain.ItemDiaryListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class DiaryFragment : Fragment(), ItemDiaryListener {

    private var _binding: FragmentDiaryBinding? = null
    private lateinit var db: DiaryDatabase
    private lateinit var adapter: ItemDiaryAdapter
    private lateinit var preferenceDataStore: DiaryPreferenceDataStore
    private val binding get() = _binding!!
    private var isSortedByTitle = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        preferenceDataStore = DiaryPreferenceDataStore.getInstance(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DiaryDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            readDiaries()
            preferenceDataStore.sortDiary.collect { sortDiary ->
                isSortedByTitle = sortDiary
                sortDiaries()
            }
        }

        binding.etSearchDiary.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onSearch()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.ivSortingDiary.setOnClickListener {
            isSortedByTitle = !isSortedByTitle
            lifecycleScope.launch {
                preferenceDataStore.setSortDiary(isSortedByTitle)
                sortDiaries()
            }
        }

        binding.ivProfileDiary.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(requireContext(),
                R.anim.fade_in,
                R.anim.fade_out
            )
            startActivity(intent, options.toBundle())
        }

        binding.fabAddNotes.setOnClickListener {
            val intent = Intent(requireActivity(), ItemDiaryInputActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_DIARY)
        }
    }

    private suspend fun sortDiaries() {
        val diaries = if (isSortedByTitle) {
            db.diaryDao().getDiariesSortedByTitle()
        } else {
            db.diaryDao().getDiary()
        }
        adapter.updateDiaries(diaries)

        if (diaries.isEmpty()) {
            binding.layoutNoData.visibility = View.VISIBLE
            binding.rvDiary.visibility = View.GONE
        } else {
            binding.layoutNoData.visibility = View.GONE
            binding.rvDiary.visibility = View.VISIBLE
        }
    }

    private suspend fun readDiaries() {
        val diaries = db.diaryDao().getDiary().toMutableList()
        binding.rvDiary.layoutManager = LinearLayoutManager(requireActivity())
        adapter = ItemDiaryAdapter(diaries, this)
        binding.rvDiary.adapter = adapter

        if (diaries.isEmpty()) {
            binding.layoutNoData.visibility = View.VISIBLE
            binding.rvDiary.visibility = View.GONE
        } else {
            binding.layoutNoData.visibility = View.GONE
            binding.rvDiary.visibility = View.VISIBLE
        }
    }

    override fun onSearch() {
        val input = binding.etSearchDiary.text.toString().trim()
        lifecycleScope.launch {
            if (input.isNotEmpty()) {
                val diaries = db.diaryDao().searchByTitle("%$input%")
                adapter.updateDiaries(diaries)
            } else {
                readDiaries()
            }
        }
    }

    override fun onEditClicked(diary: DiaryEntity) {
        val intent = Intent(requireActivity(), DetailDiary::class.java).apply {
            putExtra("id", diary.id)
            putExtra("date", diary.date)
            putExtra("title", diary.title)
            putExtra("description", diary.description)
        }
        val options = ActivityOptions.makeCustomAnimation(requireContext(),
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        startActivity(intent, options.toBundle())
    }

    override fun onDeleteClicked(diary: DiaryEntity) {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Delete confirmation")
            .setMessage("Are you sure want to delete this?")
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    db.diaryDao().deleteDiary(diary)
                    readDiaries()
                }
            }
            .setNegativeButton("No", null)
            .create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_DIARY && resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch {
                readDiaries()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            readDiaries()
        }
    }

    companion object {
        const val REQUEST_CODE_ADD_DIARY = 1
    }
}