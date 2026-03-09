package com.flash.letterly.di

import com.flash.letterly.data.repository.WordRepositoryImpl
import com.flash.letterly.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWordRepository(
        impl: WordRepositoryImpl
    ): WordRepository
}