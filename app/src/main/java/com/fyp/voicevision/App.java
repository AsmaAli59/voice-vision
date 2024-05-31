package com.fyp.voicevision;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.fyp.voicevision.helpers.utils.TextSpeechUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
        TextSpeechUtils.initTextSpeech();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


    @SuppressLint("StaticFieldLeak")
    public static Context appContext;
}
