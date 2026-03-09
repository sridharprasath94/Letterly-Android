package com.flash.letterly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flash.letterly.data.local.dao.WordListDao
import com.flash.letterly.data.local.entity.WordEntity

@Database(
    entities = [WordEntity ::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordListDao(): WordListDao
}