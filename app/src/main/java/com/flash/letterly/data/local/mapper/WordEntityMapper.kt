package com.flash.letterly.data.local.mapper

import com.flash.letterly.data.local.entity.WordEntity
import com.flash.letterly.domain.model.Word

fun WordEntity.toDomain(): Word = Word(
    value = word,
    length = word.length,
    lastAnsweredAt = lastAnsweredAt
)

fun Word.toEntity(): WordEntity =
    WordEntity(
        word = value,
        length = length,
        lastAnsweredAt = lastAnsweredAt
    )