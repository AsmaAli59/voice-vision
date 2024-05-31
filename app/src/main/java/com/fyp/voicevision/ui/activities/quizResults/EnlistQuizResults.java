package com.fyp.voicevision.ui.activities.quizResults;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityEnlistQuizResultBinding;
import com.fyp.voicevision.helpers.adapters.AdapterQuizResult;
import com.fyp.voicevision.helpers.adapters.AdapterQuizResultGeneral;
import com.fyp.voicevision.helpers.enums.LevelType;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.keys.QuizResult_Key;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.QuizResult;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnlistQuizResults extends AppCompatActivity {

    private ActivityEnlistQuizResultBinding binding;
    private AdapterQuizResult adapterQuizResult;
    private AdapterQuizResultGeneral adapterQuizResultGeneral;
    public static List<QuizResult> quizResultList;
    private int caseType = 0;
    private int levelType = 0;
    private FirebaseUser firebaseUser;
    private boolean isError = false;
    private MenuItem menuItemGraph;

    private void initializations() {
        quizResultList = new ArrayList<>();
        caseType = getIntent().getIntExtra("caseType", 0);
        levelType = getIntent().getIntExtra("levelType", 0);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        menuItemGraph = binding.toolbarEnlistQuiz.getMenu().findItem(R.id.menu_item_graph);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnlistQuizResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        updateUI();
        initRecyclerView();
        fetchContent();

        binding.toolbarEnlistQuiz.setNavigationOnClickListener(v -> finish());
        binding.toolbarEnlistQuiz.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_item_graph) {
            startActivity(new Intent(this, GraphView.class));
            return true;
        }
        return false;
    }

    /**
     * caseType
     * 0: Classroom
     * 1: General
     */

    private void updateUI() {
        //binding.mtvEmptyEnlistQuiz.setText(firebaseUser.getEmail());
        if (caseType == 0)
            binding.getRoot().setBackgroundResource(R.drawable.img_bg_quiz_classroom_history);
        else
            binding.getRoot().setBackgroundResource(R.drawable.img_bg_quiz_general_history);
    }

    private void initRecyclerView() {
        if (caseType == 0) {
            adapterQuizResult = new AdapterQuizResult(quizResultList);
            binding.rvListEnlistQuiz.setAdapter(adapterQuizResult);
        } else {
            adapterQuizResultGeneral = new AdapterQuizResultGeneral(quizResultList);
            binding.rvListEnlistQuiz.setAdapter(adapterQuizResultGeneral);
        }
    }

    private void fetchContent() {
        if (VoiceManager.isVoiceMode(this)) {
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_fetching_data));
        }
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fillList(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(EnlistQuizResults.this, error.getMessage());
            }
        };
        Query query;
        if (caseType == 0) {
            query = FirebaseUtils.dr_quiz_result.orderByChild(QuizResult_Key.QUIZ_TYPE).equalTo("Classroom");
        } else {
            query = FirebaseUtils.dr_quiz_result.orderByChild(QuizResult_Key.QUIZ_TYPE).equalTo("Quiz");

        }
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fillList(@NonNull DataSnapshot snapshot) {
        quizResultList.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            QuizResult quizResult = dataSnapshot.getValue(QuizResult.class);
            if (quizResult != null) {
                if (quizResult.getUid().equals(firebaseUser.getUid())) {
                    if (levelType == 0) {
                        quizResultList.add(quizResult);
                    } else if (levelType == 1 && quizResult.getLevelType().equals(LevelType.basic.toString())) {
                        quizResultList.add(quizResult);
                    } else if (levelType == 2 && quizResult.getLevelType().equals(LevelType.intermediate.toString())) {
                        quizResultList.add(quizResult);
                    } else if (levelType == 3 && quizResult.getLevelType().equals(LevelType.advance.toString())) {
                        quizResultList.add(quizResult);
                    }
                }
            }
        }

        if (caseType == 0) adapterQuizResult.notifyDataSetChanged();
        else adapterQuizResultGeneral.notifyDataSetChanged();
        binding.progressBarEnlistQuiz.setVisibility(View.GONE);
        if (quizResultList.isEmpty()) {
            menuItemGraph.setVisible(false);
            binding.mtvEmptyEnlistQuiz.setVisibility(View.VISIBLE);
        } else {
            menuItemGraph.setVisible(true);
            binding.mtvEmptyEnlistQuiz.setVisibility(View.GONE);
        }
        startSpeakingList();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isError) {
            initVoiceMode();
            startSpeakingList();
        } else {
            isError = false;
        }
    }

    private void startSpeakingList() {
        if (!VoiceManager.isVoiceMode(this)) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }

        TextSpeechUtils.stopSpeech();
        if (quizResultList.isEmpty()) {
            TextSpeechUtils.textSpeech("No Data Found");
        } else {
            StringBuilder text = new StringBuilder();

            text.append("To go back app, say Go Back, ");
            text.append("To exit voice mode, say Exit Voice Mode, ");
            text.append("To repeat, say repeat menu, ");
            text.append("Speak One or any other number to get results of quiz");

            TextSpeechUtils.textSpeech(text.toString(), this::launchGoogleAssistant);
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
            if (0 < quizResultList.size()) onItemClick(quizResultList.get(0));
        } else if (text.toLowerCase().contains("two") || text.toLowerCase().contains("2")) {
            if (1 < quizResultList.size()) onItemClick(quizResultList.get(1));
        } else if (text.toLowerCase().contains("three") || text.toLowerCase().contains("3")) {
            if (2 < quizResultList.size()) onItemClick(quizResultList.get(2));
        } else if (text.toLowerCase().contains("four") || text.toLowerCase().contains("4")) {
            if (3 < quizResultList.size()) onItemClick(quizResultList.get(3));
        } else if (text.toLowerCase().contains("five") || text.toLowerCase().contains("5")) {
            if (4 < quizResultList.size()) onItemClick(quizResultList.get(4));
        } else if (text.toLowerCase().contains("six") || text.toLowerCase().contains("6")) {
            if (5 < quizResultList.size()) onItemClick(quizResultList.get(5));
        } else if (text.toLowerCase().contains("seven") || text.toLowerCase().contains("7")) {
            if (6 < quizResultList.size()) onItemClick(quizResultList.get(6));
        } else if (text.toLowerCase().contains("eight") || text.toLowerCase().contains("8")) {
            if (7 < quizResultList.size()) onItemClick(quizResultList.get(7));
        } else if (text.toLowerCase().contains("nine") || text.toLowerCase().contains("9")) {
            if (8 < quizResultList.size()) onItemClick(quizResultList.get(8));
        } else if (text.toLowerCase().contains("ten") || text.toLowerCase().contains("10")) {
            if (9 < quizResultList.size()) onItemClick(quizResultList.get(9));
        } else if (text.toLowerCase().contains("eleven") || text.toLowerCase().contains("11")) {
            if (10 < quizResultList.size()) onItemClick(quizResultList.get(10));
        } else if (text.toLowerCase().contains("twelve") || text.toLowerCase().contains("12")) {
            if (11 < quizResultList.size()) onItemClick(quizResultList.get(11));
        } else if (text.toLowerCase().contains("thirteen") || text.toLowerCase().contains("13")) {
            if (12 < quizResultList.size()) onItemClick(quizResultList.get(12));
        } else if (text.toLowerCase().contains("fourteen") || text.toLowerCase().contains("14")) {
            if (13 < quizResultList.size()) onItemClick(quizResultList.get(13));
        } else if (text.toLowerCase().contains("fifteen") || text.toLowerCase().contains("15")) {
            if (14 < quizResultList.size()) onItemClick(quizResultList.get(14));
        } else if (text.toLowerCase().contains("sixteen") || text.toLowerCase().contains("16")) {
            if (15 < quizResultList.size()) onItemClick(quizResultList.get(15));
        } else if (text.toLowerCase().contains("seventeen") || text.toLowerCase().contains("17")) {
            if (16 < quizResultList.size()) onItemClick(quizResultList.get(16));
        } else if (text.toLowerCase().contains("eighteen") || text.toLowerCase().contains("18")) {
            if (17 < quizResultList.size()) onItemClick(quizResultList.get(17));
        } else if (text.toLowerCase().contains("nineteen") || text.toLowerCase().contains("19")) {
            if (18 < quizResultList.size()) onItemClick(quizResultList.get(18));
        } else if (text.toLowerCase().contains("twenty") || text.toLowerCase().contains("20")) {
            if (19 < quizResultList.size()) onItemClick(quizResultList.get(19));
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }

    private void onItemClick(QuizResult quizResult) {
        String text = quizResult.getNoOfQuestions().toLowerCase() +
                quizResult.getCorrectAnswers().toLowerCase() +
                quizResult.getScoresPercentage().toLowerCase();
        isError = true;
        TextSpeechUtils.textSpeech(text, this::startSpeakingList);
    }
}