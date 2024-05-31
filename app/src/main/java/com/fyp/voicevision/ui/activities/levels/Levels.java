package com.fyp.voicevision.ui.activities.levels;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityLevelsBinding;
import com.fyp.voicevision.helpers.adapters.AdapterLevels;
import com.fyp.voicevision.helpers.dataProviders.DpLevels;
import com.fyp.voicevision.helpers.enums.LevelType;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnHomeItemClickListener;
import com.fyp.voicevision.helpers.keys.User_Key;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.HomeItem;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.activities.home.classrooms.EnlistClassrooms;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Levels extends AppCompatActivity implements OnHomeItemClickListener {

    private ActivityLevelsBinding binding;
    private FirebaseUser firebaseUser;
    private DpLevels dataProvider;
    private ProgressDialog progressDialog;
    private boolean isUpdate = false;
    private boolean isError = false;

    private void initializations() {
        TextSpeechUtils.stopSpeech();
        dataProvider = new DpLevels();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = DialogUtils.buildProgressDialog(this, "Please Wait!", "Processing...", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        updateUI();
        initRecyclerView();

        binding.ifvBackLevels.setOnClickListener(v -> finish());
    }

    private void updateUI() {
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        if (isUpdate)
            binding.ifvBackLevels.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        AdapterLevels adapter = new AdapterLevels(dataProvider.getLevelList(), this);
        binding.rvFeaturesLevels.setAdapter(adapter);
    }

    @Override
    public void onItemClick(HomeItem homeItem) {
        progressDialog.show();
        TextSpeechUtils.stopSpeech();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            showToast(this, "Something went Wrong!");
            FirebaseUtils.logout(this);
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        if (homeItem.getId() == 0)
            hashMap.put(User_Key.LEVEL_TYPE, LevelType.basic);
        else if (homeItem.getId() == 1)
            hashMap.put(User_Key.LEVEL_TYPE, LevelType.intermediate);
        else
            hashMap.put(User_Key.LEVEL_TYPE, LevelType.advance);
        SharedPreferenceUtils.setLevel(this, String.valueOf(hashMap.get(User_Key.LEVEL_TYPE)));
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateUser();
                navigateScreen();
            } else {
                showToast(this, String.valueOf(task.getException()));
            }
        });
    }

    private void updateUser() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(User_Key.CURRENT_COURSE, 1);
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).updateChildren(hashMap);
    }

    private void navigateScreen() {
        if (!isUpdate)
            startActivity(new Intent(this, EnlistClassrooms.class));
        finish();
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

        TextSpeechUtils.textSpeech(getString(R.string.voice_mode_levels), this::launchGoogleAssistant);

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
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
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
        if (text.toLowerCase().contains("beginner")) {
            onItemClick(dataProvider.getLevelList().get(0));
        } else if (text.toLowerCase().contains("intermediate")) {
            onItemClick(dataProvider.getLevelList().get(1));
        } else if (text.toLowerCase().contains("advance")) {
            onItemClick(dataProvider.getLevelList().get(2));
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