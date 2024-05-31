package com.fyp.voicevision.ui.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.databinding.ActivityMainBinding;
import com.fyp.voicevision.helpers.enums.ModeType;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.ui.activities.authentication.Login;
import com.fyp.voicevision.ui.activities.home.Home;
import com.fyp.voicevision.ui.activities.modes.Modes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setHandler();
    }

    // Move Screen
    private void setHandler() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::navigateScreen, 2000);
    }

    private void navigateScreen() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            if (SharedPreferenceUtils.getMode(this).equals(ModeType.none.toString()))
                startActivity(new Intent(MainActivity.this, Modes.class));
            else
                startActivity(new Intent(MainActivity.this, Home.class));
        } else
            startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }
}