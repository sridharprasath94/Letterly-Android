package com.flash.letterly.domain.model

data class Word(
    val value: String,
    val length : Int,
    val lastAnsweredAt: Long?
)