package com.cthacademy.android.modal

data class LessonVideoModal(
    val id: String,
    val userPhone: String,
    val uri: String,
    val image: String,
    val title: String,
    val description: String,
    val isLocked: Boolean,
    val point: Long
)
