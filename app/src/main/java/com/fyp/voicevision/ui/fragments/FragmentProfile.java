package com.fyp.voicevision.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.FragmentProfileBinding;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.User;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.activities.home.Home;
import com.fyp.voicevision.ui.activities.quizResults.EnlistQuizResults;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentProfile extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseUser firebaseUser;
    private boolean isError = false;

    private void initializations() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializations();
        updateUI();

        binding.mbViewAllClassroomProfile.setOnClickListener(v -> onQuizClick(0, 0));
        binding.mtvBasicClassroomProfile.setOnClickListener(v -> onQuizClick(0, 1));
        binding.mtvIntermediateClassroomProfile.setOnClickListener(v -> onQuizClick(0, 2));
        binding.mtvAdvanceClassroomProfile.setOnClickListener(v -> onQuizClick(0, 3));

        binding.mbViewAllGeneralProfile.setOnClickListener(v -> onQuizClick(1, 0));
        binding.mtvBasicGeneralProfile.setOnClickListener(v -> onQuizClick(1, 1));
        binding.mtvIntermediateGeneralProfile.setOnClickListener(v -> onQuizClick(1, 2));
        binding.mtvAdvanceGeneralProfile.setOnClickListener(v -> onQuizClick(1, 3));

        binding.mbLogoutProfile.setOnClickListener(v -> FirebaseUtils.logout(getActivity()));
    }

    private void updateUI() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    String mode = "Mode: " + user.getModeType();
                    binding.mtvNameProfile.setText(user.getFullName());
                    binding.mtvModesProfile.setText(mode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).addValueEventListener(valueEventListener);
    }

    private void onQuizClick(int caseType, int levelType) {
        Intent intent = new Intent(getContext(), EnlistQuizResults.class);
        intent.putExtra("caseType", caseType);
        intent.putExtra("levelType", levelType);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isError) {
            initVoiceMode();
        } else {
            isError = false;
        }
    }

    /* ------------------------------------------- Voice Mode ------------------------------------------- */

    private void initVoiceMode() {
        if (!VoiceManager.isVoiceMode(getContext())) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }
        binding.incVoice.getRoot().setVisibility(View.VISIBLE);

        TextSpeechUtils.textSpeech(getString(R.string.voice_mode_profile), this::launchGoogleAssistant);

        binding.incVoice.getRoot().setOnClickListener(v -> launchGoogleAssistant());
        binding.incVoice.mbExitVoiceMode.setOnClickListener(v -> VoiceManager.exitVoiceMode(getActivity()));
    }

    private void launchGoogleAssistant() {
        TextSpeechUtils.stopSpeech();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter Command");
        activityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                ArrayList<String> resultList = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String text = resultList.get(0);
                boolean isNormalCommand = VoiceManager.checkDefaultCommand(getActivity(), text);
                if (isNormalCommand) {
                    navigateScreen(text);
                }
            }
        }
    });

    private void navigateScreen(String text) {
        if (text.toLowerCase().contains("repeat")) {
            initVoiceMode();
        } else if (text.toLowerCase().contains("home")) {
            ((Home) getActivity()).loadFragment(new FragmentHome());
        } else if (text.toLowerCase().contains("view all classroom")) {
            onQuizClick(0, 0);
        } else if (text.toLowerCase().contains("basic classroom")) {
            onQuizClick(0, 1);
        } else if (text.toLowerCase().contains("intermediate classroom")) {
            onQuizClick(0, 2);
        } else if (text.toLowerCase().contains("advance classroom")) {
            onQuizClick(0, 3);
        } else if (text.toLowerCase().contains("view all general")) {
            onQuizClick(1, 0);
        } else if (text.toLowerCase().contains("basic general")) {
            onQuizClick(1, 1);
        } else if (text.toLowerCase().contains("intermediate general")) {
            onQuizClick(1, 2);
        } else if (text.toLowerCase().contains("advance general")) {
            onQuizClick(1, 3);
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }

    @Override
    public void onDestroy() {
        TextSpeechUtils.stopSpeech();
        super.onDestroy();
    }
}