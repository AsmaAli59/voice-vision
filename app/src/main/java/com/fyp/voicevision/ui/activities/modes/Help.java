package com.fyp.voicevision.ui.activities.modes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.databinding.ActivityHelpBinding;

public class Help extends AppCompatActivity {

    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.ifvBackHelp.setOnClickListener(v -> finish());
    }
}