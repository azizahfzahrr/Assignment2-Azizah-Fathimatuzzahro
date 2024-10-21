package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.domain

import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.data.DiaryEntity

interface ItemDiaryListener {
    fun onSearch()
    fun onEditClicked(diary: DiaryEntity)
    fun onDeleteClicked(diary: DiaryEntity)
}