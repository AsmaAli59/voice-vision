package com.fyp.voicevision.helpers.utils;

import static com.fyp.voicevision.App.appContext;
import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.fyp.voicevision.helpers.interfaces.OnTextCompletionListener;

import java.util.Locale;

public class TextSpeechUtils {

    public static TextToSpeech textToSpeech;
    private static OnTextCompletionListener completionListener;

    public static void initTextSpeech() {
        textToSpeech = new TextToSpeech(appContext, status -> {
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {

                }

                @Override
                public void onDone(String utteranceId) {
                    completionListener.onDone();
                }

                @Override
                public void onError(String utteranceId) {
                    showToast(appContext, "error");
                }
            });
        });

        // Speed of speaking
        textToSpeech.setSpeechRate(0.8f);
        textToSpeech.setLanguage(Locale.US);
    }

    public static void textSpeech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static void textSpeech(String text, OnTextCompletionListener onTextCompletionListener) {
        completionListener = onTextCompletionListener;
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }


    public static void stopSpeech() {
        textToSpeech.stop();
    }

}
