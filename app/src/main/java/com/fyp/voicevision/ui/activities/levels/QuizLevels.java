package com.fyp.voicevision.ui.activities.levels;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.databinding.ActivityQuizLevelsBinding;
import com.fyp.voicevision.helpers.adapters.AdapterLevels;
import com.fyp.voicevision.helpers.dataProviders.DpLevels;
import com.fyp.voicevision.helpers.enums.LevelType;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnHomeItemClickListener;
import com.fyp.voicevision.helpers.keys.User_Key;
import com.fyp.voicevision.helpers.models.HomeItem;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.activities.home.quiz.EnlistQuiz;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class QuizLevels extends AppCompatActivity implements OnHomeItemClickListener {

    private ActivityQuizLevelsBinding binding;
    private DpLevels dataProvider;
    private ProgressDialog progressDialog;
    private boolean isUpdate = false;

    private void initializations() {
        TextSpeechUtils.stopSpeech();
        dataProvider = new DpLevels();
        progressDialog = DialogUtils.buildProgressDialog(this, "Please Wait!", "Processing...", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizLevelsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        updateUI();
        initRecyclerView();
    }

    private void updateUI() {
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
    }

    private void initRecyclerView() {
        AdapterLevels adapter = new AdapterLevels(dataProvider.getLevelList(), this);
        binding.rvFeaturesQuizLevels.setAdapter(adapter);
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
            hashMap.put(User_Key.QUIZ_LEVEL_TYPE, LevelType.basic);
        else if (homeItem.getId() == 1)
            hashMap.put(User_Key.QUIZ_LEVEL_TYPE, LevelType.intermediate);
        else
            hashMap.put(User_Key.QUIZ_LEVEL_TYPE, LevelType.advance);
        SharedPreferenceUtils.setQuizLevel(this, String.valueOf(hashMap.get(User_Key.QUIZ_LEVEL_TYPE)));
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                navigateScreen();
            } else {
                showToast(this, String.valueOf(task.getException()));
            }
        });
    }

    private void navigateScreen() {
        if (!isUpdate)
            startActivity(new Intent(this, EnlistQuiz.class));
        finish();
    }
}