package com.fyp.voicevision.helpers.manager;

import android.app.Activity;
import android.content.Context;

import com.fyp.voicevision.helpers.enums.ModeType;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;

public class VoiceManager {

    public static boolean isVoiceMode(Context context) {
        String mode = SharedPreferenceUtils.getMode(context);
        return mode.equals(ModeType.voice.toString());
    }

    public static void exitVoiceMode(Activity activity) {
        SharedPreferenceUtils.setMode(activity, ModeType.touch.toString());
        activity.recreate();
    }

    public static boolean checkDefaultCommand(Activity activity, String text) {
        if (text.equalsIgnoreCase("exit")) {
            activity.finish();
            return false;
        } else if (text.equalsIgnoreCase("exit voice mode")) {
            exitVoiceMode(activity);
            return false;
        } else {
            return true;
        }
    }
}