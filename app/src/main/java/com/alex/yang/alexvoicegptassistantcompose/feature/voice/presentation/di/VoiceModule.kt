package com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation.di

import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.repository.VoiceRepositoryImpl
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.domain.repository.VoiceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class VoiceModule {
    @Binds
    @Singleton
    abstract fun bindVoiceRepository(impl: VoiceRepositoryImpl): VoiceRepository
}