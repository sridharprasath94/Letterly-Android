package com.flash.letterly

import android.app.Application
import com.flash.letterly.data.local.WordSeeder
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class LetterlyApp : Application() {

    @Inject
    lateinit var seeder: WordSeeder

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            seeder.seedIfNeeded()
        }
    }
}