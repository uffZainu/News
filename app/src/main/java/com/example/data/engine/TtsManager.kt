package com.example.data.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TtsManager(context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentArticleId = MutableStateFlow<String?>(null)
    val currentArticleId: StateFlow<String?> = _currentArticleId.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.language = Locale.ENGLISH
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isPlaying.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isPlaying.value = false
                        _currentArticleId.value = null
                    }

                    override fun onError(utteranceId: String?) {
                        _isPlaying.value = false
                        _currentArticleId.value = null
                    }
                })
            }
        }
    }

    fun speak(articleId: String, text: String, languageCode: String = "en") {
        if (!isInitialized || tts == null) return

        if (_isPlaying.value && _currentArticleId.value == articleId) {
            stop()
            return
        }

        stop()
        _currentArticleId.value = articleId

        val locale = when (languageCode) {
            "hi" -> Locale("hi", "IN")
            "bn" -> Locale("bn", "IN")
            "ta" -> Locale("ta", "IN")
            "te" -> Locale("te", "IN")
            else -> Locale.ENGLISH
        }
        tts?.language = locale

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "NovyraTTS_$articleId")
        _isPlaying.value = true
    }

    fun stop() {
        tts?.stop()
        _isPlaying.value = false
        _currentArticleId.value = null
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
    }
}
