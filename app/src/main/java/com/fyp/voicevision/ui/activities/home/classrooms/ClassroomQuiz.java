package com.fyp.voicevision.ui.activities.home.classrooms;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityClassroomQuizBinding;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.keys.CourseQuizItem_Key;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.CourseItem;
import com.fyp.voicevision.helpers.models.CourseQuizItem;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.dialogs.DialogResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClassroomQuiz extends AppCompatActivity {

    private ActivityClassroomQuizBinding binding;
    private List<CourseQuizItem> courseQuizItem;
    private ProgressDialog progressDialog;
    private CourseItem courseItem;

    private int counter;
    private boolean isError = false;
    private boolean isFetched = false;
    private DialogResult dialogResult;

    private void initializations() {
        TextSpeechUtils.stopSpeech();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            courseItem = getIntent().getSerializableExtra("course", CourseItem.class);
        } else {
            courseItem = (CourseItem) getIntent().getSerializableExtra("course");
        }
        progressDialog = DialogUtils.buildProgressDialog(this, "Please Wait!", "Processing...", false);
        courseQuizItem = new ArrayList<>();
        counter = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassroomQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        setUI();
        fetchList();
        binding.toolbarClassroomQuiz.setNavigationOnClickListener(v -> finish());
        binding.mbOneClassroomQuiz.setOnClickListener(v -> onOptionSelection(v, 0));
        binding.mbTwoClassroomQuiz.setOnClickListener(v -> onOptionSelection(v, 1));
        binding.mbThreeClassroomQuiz.setOnClickListener(v -> onOptionSelection(v, 2));
        binding.mbFourClassroomQuiz.setOnClickListener(v -> onOptionSelection(v, 3));
        binding.mbNextClassroomQuiz.setOnClickListener(v -> onNextClick());
    }

    private void setUI() {
        binding.toolbarClassroomQuiz.setTitle(courseItem.getTitle());
    }

    private void fetchList() {
        progressDialog.show();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fillList(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(ClassroomQuiz.this, error.getMessage());
            }
        };
        Query query = FirebaseUtils.dr_course_quiz.orderByChild(CourseQuizItem_Key.CID).equalTo(courseItem.getCid());
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fillList(@NonNull DataSnapshot snapshot) {
        isFetched = true;
        courseQuizItem.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            CourseQuizItem item = dataSnapshot.getValue(CourseQuizItem.class);
            if (item != null) {
                courseQuizItem.add(item);
            }
        }
        progressDialog.dismiss();
        startSpeakingList();
        if (courseQuizItem.isEmpty()) {
            showToast(this, "No Quiz Found");
            finish();
        } else {
            initTimer();
            updateUI();
        }
    }

    private void initTimer() {
        long totalTimeInMillis = courseQuizItem.size() * 30L * 1000L;
        new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                String time = String.format(Locale.getDefault(), "%02d:%02d", secondsRemaining / 60, secondsRemaining % 60);
                binding.mtvTimerClassroomQuiz.setText(time);
            }

            @Override
            public void onFinish() {
                counter = courseQuizItem.size() + 1;
                onNextClick();
            }
        }.start();
    }

    private void updateUI() {
        CourseQuizItem item = courseQuizItem.get(counter);
        binding.mtvHeadingClassroomQuiz.setText(getString(R.string._1_5, (counter + 1), courseQuizItem.size()));
        binding.mtvTitleClassroomQuiz.setText(getString(R.string.question_no_1, (counter + 1)));
        binding.mtvQuestionClassroomQuiz.setText(item.getQuestion());
        binding.mbOneClassroomQuiz.setText("A) " + item.getOptionA());
        binding.mbTwoClassroomQuiz.setText("B) " + item.getOptionB());
        binding.mbThreeClassroomQuiz.setText("C) " + item.getOptionC());
        binding.mbFourClassroomQuiz.setText("D) " + item.getOptionD());
    }

    private void onOptionSelection(View view, int selectionOption) {
        buttonEnable(false);
        int correctOption = courseQuizItem.get(counter).getCorrectOption();
        if (selectionOption == correctOption) {
            courseQuizItem.get(counter).setPass(true);
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
            switch (correctOption) {
                case 0: {
                    binding.mbOneClassroomQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                    break;
                }
                case 1: {
                    binding.mbTwoClassroomQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                    break;
                }
                case 2: {
                    binding.mbThreeClassroomQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                    break;
                }
                case 3: {
                    binding.mbFourClassroomQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                    break;
                }
            }
        }
        if (VoiceManager.isVoiceMode(this)) {
            onNextClick();
        }
    }

    private void buttonEnable(boolean enableButton) {
        binding.mbOneClassroomQuiz.setEnabled(enableButton);
        binding.mbTwoClassroomQuiz.setEnabled(enableButton);
        binding.mbThreeClassroomQuiz.setEnabled(enableButton);
        binding.mbFourClassroomQuiz.setEnabled(enableButton);
        binding.mbNextClassroomQuiz.setEnabled(!enableButton);
        if (enableButton) {
            binding.mbOneClassroomQuiz.setBackgroundColor(0);
            binding.mbTwoClassroomQuiz.setBackgroundColor(0);
            binding.mbThreeClassroomQuiz.setBackgroundColor(0);
            binding.mbFourClassroomQuiz.setBackgroundColor(0);
        }
    }

    private void onNextClick() {
        counter++;
        if (counter >= courseQuizItem.size()) {
            speakResult();
            showResult();
            return;
        }
        updateUI();
        buttonEnable(true);
    }

    private void speakResult() {
        isError = true;
        if (!VoiceManager.isVoiceMode(this)) {
            return;
        }

        int scores = getCorrectAnswers();
        float size = courseQuizItem.size();
        int percentage = Math.round(((scores / size) * 100));

        StringBuilder sb = new StringBuilder();
        sb.append("You have answered ").append(courseQuizItem.size()).append(" questions").append(", ");
        sb.append(scores).append(" of them are correct answers").append(", ");
        sb.append("Your total percentage is ").append(percentage).append(", ");
        sb.append("Say Exit quiz to finish quiz").append(", ");
        sb.append("Say Replay quiz to replay quiz");

        TextSpeechUtils.textSpeech(sb.toString(), this::launchGoogleAssistant);
    }

    private int getCorrectAnswers() {
        int scores = 0;
        for (CourseQuizItem item : courseQuizItem) {
            if (item.isPass()) {
                scores++;
            }
        }
        return scores;
    }

    private void showResult() {
        dialogResult = new DialogResult(courseItem, courseQuizItem, courseItem.getLevelType(), this::recreate);
        dialogResult.show(getSupportFragmentManager(), "fragment_manager");
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
        if (!isFetched) return;
        if (!VoiceManager.isVoiceMode(this)) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }

        TextSpeechUtils.stopSpeech();
        if (courseQuizItem.isEmpty()) {
            TextSpeechUtils.textSpeech("No Quiz Available");
        } else {
            StringBuilder text = new StringBuilder();
            if (counter == 0) {
                text.append("To go back app, say Go Back, ");
                text.append("To exit voice mode, say Exit Voice Mode, ");
                text.append("To repeat, say repeat question, ");
            }

            continueText(text);
        }
    }

    private void continueText(StringBuilder text) {
        if (counter >= courseQuizItem.size()) return;
        CourseQuizItem item = courseQuizItem.get(counter);
        if (counter == 0) {
            text.append("Your question is: ").append(item.getQuestion()).append(", ");
        } else {
            text.append("Next question is: ").append(item.getQuestion()).append(", ");
        }
        text.append("Option A) ").append(item.getOptionA()).append(", ");
        text.append("Option B) ").append(item.getOptionB()).append(", ");
        text.append("Option C) ").append(item.getOptionC()).append(", ");
        text.append("Option D) ").append(item.getOptionD()).append(", ");
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                TextSpeechUtils.textSpeech(text.toString(), this::launchGoogleAssistant), 2000);
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
                    selectOption(text);
                }
            }
        }
    });

    private void selectOption(String text) {
        if (text.toLowerCase().contains("replay")) {
            dialogResult.dismiss();
            recreate();
        } else if (text.toLowerCase().contains("exit")) {
            dialogResult.dismiss();
            isError = false;
            finish();
        } else if (text.toLowerCase().contains("repeat")) {
            initVoiceMode();
            } else if (text.toLowerCase().contains("hold")) {

            } else if (text.toLowerCase().contains("back")) {
                finish();
        }  else if (text.toLowerCase().contains(" a")) {
            onOptionSelection(binding.mbOneClassroomQuiz, 0);
        } else if (text.toLowerCase().contains(" b")) {
            onOptionSelection(binding.mbTwoClassroomQuiz, 1);
        } else if (text.toLowerCase().contains(" c")) {
            onOptionSelection(binding.mbThreeClassroomQuiz, 2);
        } else if (text.toLowerCase().contains(" d")) {
            onOptionSelection(binding.mbFourClassroomQuiz, 3);
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }
}