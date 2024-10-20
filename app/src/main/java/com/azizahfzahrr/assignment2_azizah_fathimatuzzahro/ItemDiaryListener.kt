package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

interface ItemDiaryListener {
    fun onSearch()
    fun onEditClicked(diary: DiaryEntity)
    fun onDeleteClicked(diary: DiaryEntity)
}