package com.fyp.voicevision.ui.activities.modes;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityModesBinding;
import com.fyp.voicevision.helpers.adapters.AdapterModes;
import com.fyp.voicevision.helpers.dataProviders.DpModes;
import com.fyp.voicevision.helpers.enums.ModeType;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnHomeItemClickListener;
import com.fyp.voicevision.helpers.keys.User_Key;
import com.fyp.voicevision.helpers.models.HomeItem;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.activities.home.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Objects;

public class Modes extends AppCompatActivity implements OnHomeItemClickListener {

    private ActivityModesBinding binding;
    private DpModes dataProvider;
    private ProgressDialog progressDialog;
    private boolean isUpdate = false;

    private void initializations() {
        dataProvider = new DpModes();
        progressDialog = DialogUtils.buildProgressDialog(this, "Please Wait!", "Processing...", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        updateUI();
        initRecyclerView();

        binding.ifvBackModes.setOnClickListener(v -> finish());
        binding.ifvHelpModes.setOnClickListener(v -> onHelpClick());
    }

    private void updateUI() {
        if (!Objects.equals(SharedPreferenceUtils.getMode(this), ModeType.none.toString())) {
            isUpdate = true;
            binding.ifvBackModes.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        AdapterModes adapter = new AdapterModes(dataProvider.getModeList(), this);
        binding.rvFeaturesModes.setAdapter(adapter);
    }

    private void onHelpClick() {
        startActivity(new Intent(this, Help.class));
    }

    @Override
    public void onItemClick(HomeItem homeItem) {
        progressDialog.show();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            showToast(this, "Something went Wrong!");
            FirebaseUtils.logout(this);
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        if (homeItem.getId() == 0)
            hashMap.put(User_Key.MODE_TYPE, ModeType.touch);
        else
            hashMap.put(User_Key.MODE_TYPE, ModeType.voice);
        SharedPreferenceUtils.setMode(this, String.valueOf(hashMap.get(User_Key.MODE_TYPE)));
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                navigateScreen();
            } else {
                showToast(this, String.valueOf(task.getException()));
            }
        });
    }

    private void navigateScreen() {
        progressDialog.dismiss();
        if (SharedPreferenceUtils.getMode(this).equals(ModeType.voice.toString())) {
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_start), this::launchScreen);
        } else {
            launchScreen();
        }
    }

    private void launchScreen() {
        if (isUpdate) {
            finish();
            recreate();
        } else
            startActivity(new Intent(Modes.this, Home.class));
    }
}