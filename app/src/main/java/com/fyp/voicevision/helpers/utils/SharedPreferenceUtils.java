package com.fyp.voicevision.helpers.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

    private static final String MY_PREF = "my_pref";
    private static final String MODE_TYPE = "modeType";
    private static final String LEVEL_TYPE = "levelType";
    private static final String QUIZ_LEVEL_TYPE = "quizLevelType";

    public static String getMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MODE_TYPE, "none");
    }

    public static void setMode(Context context, String modeType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MODE_TYPE, modeType);
        editor.apply();
    }

    public static String getLevel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LEVEL_TYPE, "none");
    }

    public static void setLevel(Context context, String levelType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LEVEL_TYPE, levelType);
        editor.apply();
    }

    // Quiz
    public static String getQuizLevel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(QUIZ_LEVEL_TYPE, "none");
    }

    public static void setQuizLevel(Context context, String levelType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(QUIZ_LEVEL_TYPE, levelType);
        editor.apply();
    }

}
