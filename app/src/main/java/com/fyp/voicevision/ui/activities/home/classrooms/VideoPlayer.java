package com.fyp.voicevision.ui.activities.home.classrooms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.MediaController;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityVideoPlayerBinding;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.Lesson;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;

import java.util.ArrayList;
import java.util.Locale;

public class VideoPlayer extends AppCompatActivity {

    private ActivityVideoPlayerBinding binding;
    private Lesson lesson;
    private boolean isError = false;

    private void initializations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            lesson = getIntent().getSerializableExtra("lesson", Lesson.class);
        } else {
            lesson = (Lesson) getIntent().getSerializableExtra("lesson");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        updateUI();
        initVideoPlayer();

        binding.toolbarVideoPlayer.setNavigationOnClickListener(v -> finish());
    }

    private void updateUI() {
        binding.toolbarVideoPlayer.setTitle(lesson.getTitle());
    }

    private void initVideoPlayer() {
        if (lesson == null) {
            return;
        }
        Uri uri = Uri.parse(lesson.getVideoUrl());
        binding.vvVideoVideoPlayer.setVideoURI(uri);
        MediaController mediaController = new MediaController(VideoPlayer.this);
        mediaController.setAnchorView(binding.vvVideoVideoPlayer);
        mediaController.setMediaPlayer(binding.vvVideoVideoPlayer);
        binding.vvVideoVideoPlayer.setOnPreparedListener(mp -> binding.progressBarVideoPlayer.setVisibility(View.GONE));
        binding.vvVideoVideoPlayer.setOnCompletionListener(mp -> finish());
        binding.vvVideoVideoPlayer.setMediaController(mediaController);
        binding.vvVideoVideoPlayer.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isError) {
            initVoiceMode();
        } else {
            isError = false;
        }
    }

    /* ------------------------------------------- Voice Mode ------------------------------------------- */

    private void initVoiceMode() {
        if (!VoiceManager.isVoiceMode(this)) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }
        binding.incVoice.getRoot().setVisibility(View.VISIBLE);

        TextSpeechUtils.textSpeech(getString(R.string.voice_mode_player));

        binding.incVoice.getRoot().setOnClickListener(v -> launchGoogleAssistant());
        binding.incVoice.mbExitVoiceMode.setOnClickListener(v -> VoiceManager.exitVoiceMode(this));
    }

    private void launchGoogleAssistant() {
        TextSpeechUtils.stopSpeech();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter Command");
        activityResultLauncher.launch(intent);
        binding.vvVideoVideoPlayer.pause();
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            binding.vvVideoVideoPlayer.start();
            Intent intent = result.getData();
            if (intent != null) {
                ArrayList<String> resultList = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String text = resultList.get(0);
                boolean isNormalCommand = VoiceManager.checkDefaultCommand(this, text);
                if (isNormalCommand) {
                    navigateScreen(text);
                }
            }
        }
    });

    private void navigateScreen(String text) {
        if (text.toLowerCase().contains("back")) {
            binding.vvVideoVideoPlayer.stopPlayback();
            finish();
        } else if (text.toLowerCase().contains("pause")) {
            binding.vvVideoVideoPlayer.pause();
        } else if (text.toLowerCase().contains("play") || text.toLowerCase().contains("resume")) {
            binding.vvVideoVideoPlayer.start();
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }

    @Override
    protected void onDestroy() {
        TextSpeechUtils.stopSpeech();
        super.onDestroy();
    }
}