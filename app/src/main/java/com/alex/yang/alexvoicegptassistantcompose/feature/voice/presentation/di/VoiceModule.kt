package com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation.di

import android.content.Context
import com.alex.yang.alexvoicegptassistantcompose.core.audio.AudioPlayer
import com.alex.yang.alexvoicegptassistantcompose.core.stt.Speech2TextManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object VoiceModule {
    @Provides
    @Singleton
    fun provideSpeech2TextManager(@ApplicationContext context: Context) =
        Speech2TextManager(context)

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context) =
        AudioPlayer(context)
}