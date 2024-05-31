package com.fyp.voicevision.ui.activities.home.quiz;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityEnlistQuizBinding;
import com.fyp.voicevision.helpers.adapters.AdapterQuizTopic;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnQuizTopicItemClickListener;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.QuizTopicItem;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.activities.levels.QuizLevels;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnlistQuiz extends AppCompatActivity implements OnQuizTopicItemClickListener {

    private ActivityEnlistQuizBinding binding;
    private AdapterQuizTopic adapterCourse;
    private List<QuizTopicItem> quizTopicItems;
    private boolean isError = false;
    private boolean isFetched = false;

    private void initializations() {
        TextSpeechUtils.stopSpeech();
        quizTopicItems = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnlistQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();

        initRecyclerView();
        fetchContent();

        binding.ifvBackEnlistQuiz.setOnClickListener(v -> finish());
        binding.mbQuizLevels.setOnClickListener(v -> onQuizLevelClick());
    }

    private void updateUI() {
        String text = "Level: " + SharedPreferenceUtils.getQuizLevel(this);
        binding.mbQuizLevels.setText(text);
    }

    private void initRecyclerView() {
        adapterCourse = new AdapterQuizTopic(quizTopicItems, this);
        binding.rvFeaturesEnlistQuiz.setAdapter(adapterCourse);
    }

    private void fetchContent() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fillList(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(EnlistQuiz.this, error.getMessage());
            }
        };
        FirebaseUtils.dr_quiz_topics.addValueEventListener(valueEventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fillList(@NonNull DataSnapshot snapshot) {
        isFetched = true;
        quizTopicItems.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            QuizTopicItem courseItem = dataSnapshot.getValue(QuizTopicItem.class);
            if (courseItem != null) {
                quizTopicItems.add(courseItem);
            }
        }
        adapterCourse.notifyDataSetChanged();
        binding.progressBarEnlistQuiz.setVisibility(View.GONE);
        if (quizTopicItems.isEmpty())
            binding.mtvEmptyEnlistQuiz.setVisibility(View.VISIBLE);
        else
            binding.mtvEmptyEnlistQuiz.setVisibility(View.GONE);

        startSpeakingList();
    }

    private void onQuizLevelClick() {
        Intent intent = new Intent(this, QuizLevels.class);
        intent.putExtra("isUpdate", true);
        startActivity(intent);
    }

    @Override
    public void onItemClick(QuizTopicItem quizTopicItem) {
        Intent intent = new Intent(this, Quiz.class);
        intent.putExtra("quizTopicItem", quizTopicItem);
        startActivity(intent);
    }

    private void startSpeakingList() {
        if (!isFetched) return;
        if (!VoiceManager.isVoiceMode(this)) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }

        TextSpeechUtils.stopSpeech();
        if (quizTopicItems.isEmpty()) {
            TextSpeechUtils.textSpeech("No Data Found");
        } else {
            StringBuilder text = new StringBuilder();

            text.append("To go back app, say Go Back, ");
            text.append("To exit voice mode, say Exit Voice Mode, ");
            text.append("To repeat, say repeat menu, ");

            for (int i = 0; i < quizTopicItems.size(); i++) {
                QuizTopicItem courseItem = quizTopicItems.get(i);
                text.append("Speak Select ").append(i + 1).append(" for ").append(courseItem.getTopicName()).append("  ");
            }
            TextSpeechUtils.textSpeech(text.toString(), this::launchGoogleAssistant);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        if (!isError) {
            initVoiceMode();
            startSpeakingList();
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
        if (text.toLowerCase().contains("repeat")) {
            initVoiceMode();
        } else if (text.toLowerCase().contains("back")) {
            finish();
        } else if (text.toLowerCase().contains("one") || text.toLowerCase().contains("1")) {
            if (0 < quizTopicItems.size()) onItemClick(quizTopicItems.get(0));
        } else if (text.toLowerCase().contains("two") || text.toLowerCase().contains("2")) {
            if (1 < quizTopicItems.size()) onItemClick(quizTopicItems.get(1));
        } else if (text.toLowerCase().contains("three") || text.toLowerCase().contains("3")) {
            if (2 < quizTopicItems.size()) onItemClick(quizTopicItems.get(2));
        } else if (text.toLowerCase().contains("four") || text.toLowerCase().contains("4")) {
            if (3 < quizTopicItems.size()) onItemClick(quizTopicItems.get(3));
        } else if (text.toLowerCase().contains("five") || text.toLowerCase().contains("5")) {
            if (4 < quizTopicItems.size()) onItemClick(quizTopicItems.get(4));
        } else if (text.toLowerCase().contains("six") || text.toLowerCase().contains("6")) {
            if (5 < quizTopicItems.size()) onItemClick(quizTopicItems.get(5));
        } else if (text.toLowerCase().contains("seven") || text.toLowerCase().contains("7")) {
            if (6 < quizTopicItems.size()) onItemClick(quizTopicItems.get(6));
        } else if (text.toLowerCase().contains("eight") || text.toLowerCase().contains("8")) {
            if (7 < quizTopicItems.size()) onItemClick(quizTopicItems.get(7));
        } else if (text.toLowerCase().contains("nine") || text.toLowerCase().contains("9")) {
            if (8 < quizTopicItems.size()) onItemClick(quizTopicItems.get(8));
        } else if (text.toLowerCase().contains("ten") || text.toLowerCase().contains("10")) {
            if (9 < quizTopicItems.size()) onItemClick(quizTopicItems.get(9));
        } else if (text.toLowerCase().contains("eleven") || text.toLowerCase().contains("11")) {
            if (10 < quizTopicItems.size()) onItemClick(quizTopicItems.get(10));
        } else if (text.toLowerCase().contains("twelve") || text.toLowerCase().contains("12")) {
            if (11 < quizTopicItems.size()) onItemClick(quizTopicItems.get(11));
        } else if (text.toLowerCase().contains("thirteen") || text.toLowerCase().contains("13")) {
            if (12 < quizTopicItems.size()) onItemClick(quizTopicItems.get(12));
        } else if (text.toLowerCase().contains("fourteen") || text.toLowerCase().contains("14")) {
            if (13 < quizTopicItems.size()) onItemClick(quizTopicItems.get(13));
        } else if (text.toLowerCase().contains("fifteen") || text.toLowerCase().contains("15")) {
            if (14 < quizTopicItems.size()) onItemClick(quizTopicItems.get(14));
        } else if (text.toLowerCase().contains("sixteen") || text.toLowerCase().contains("16")) {
            if (15 < quizTopicItems.size()) onItemClick(quizTopicItems.get(15));
        } else if (text.toLowerCase().contains("seventeen") || text.toLowerCase().contains("17")) {
            if (16 < quizTopicItems.size()) onItemClick(quizTopicItems.get(16));
        } else if (text.toLowerCase().contains("eighteen") || text.toLowerCase().contains("18")) {
            if (17 < quizTopicItems.size()) onItemClick(quizTopicItems.get(17));
        } else if (text.toLowerCase().contains("nineteen") || text.toLowerCase().contains("19")) {
            if (18 < quizTopicItems.size()) onItemClick(quizTopicItems.get(18));
        } else if (text.toLowerCase().contains("twenty") || text.toLowerCase().contains("20")) {
            if (19 < quizTopicItems.size()) onItemClick(quizTopicItems.get(19));
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }
}