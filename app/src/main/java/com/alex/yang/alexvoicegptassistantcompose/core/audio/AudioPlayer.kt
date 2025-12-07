package com.alex.yang.alexvoicegptassistantcompose.core.audio

import android.content.Context
import android.media.MediaPlayer
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
@Singleton
class AudioPlayer @Inject constructor(
    val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null

    fun play(bytes: ByteArray) {
        stop()

        val tempFile = File.createTempFile("openai_", ".mp3", context.cacheDir)
        FileOutputStream(tempFile).use { it.write(bytes) }

        mediaPlayer = MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            setOnPreparedListener { start() }
            setOnCompletionListener {
                stop()
                tempFile.delete()
            }
            setOnErrorListener { _, _, _ ->
                stop()
                tempFile.delete()
                true
            }
            prepareAsync()
        }
    }

    fun stop() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
            mediaPlayer = null
        }
    }
}