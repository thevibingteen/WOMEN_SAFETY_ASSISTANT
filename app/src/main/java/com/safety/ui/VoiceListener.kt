package com.safety.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

fun startVoiceListening(
    context: Context,
    onHelpDetected: () -> Unit
): SpeechRecognizer {

    val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    recognizer.setRecognitionListener(object : RecognitionListener {

        override fun onResults(results: Bundle?) {
            val matches =
                results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            matches?.forEach { text ->
                if (text.lowercase().contains("help")) {
                    onHelpDetected()
                }
            }
        }

        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {}
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    recognizer.startListening(intent)
    return recognizer
}
