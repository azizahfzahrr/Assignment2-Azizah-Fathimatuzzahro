package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

data class Diary(
    val id: Int = 0,
    val title: String,
    val description: String,
    val date: Long = System.currentTimeMillis()
)
