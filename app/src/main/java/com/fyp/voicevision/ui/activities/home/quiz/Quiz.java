package com.fyp.voicevision.ui.activities.home.quiz;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.TAG;
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
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityQuizBinding;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnPreviewClickListener;
import com.fyp.voicevision.helpers.interfaces.OnReplayClickListener;
import com.fyp.voicevision.helpers.keys.GeneralQuizItem_Key;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.GeneralQuizItem;
import com.fyp.voicevision.helpers.models.QuizTopicItem;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.dialogs.DialogResultQuiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Quiz extends AppCompatActivity {

    public   static  boolean PREVIEW ;
    private ActivityQuizBinding binding;
    private  List<GeneralQuizItem> generalQuizItems;
    private ProgressDialog progressDialog;
    private  QuizTopicItem quizTopicItem;

    private boolean isError = false;
    private boolean isFetched = false;
    private int counter;
    private DialogResultQuiz dialogResult;

    private void initializations() {
        TextSpeechUtils.stopSpeech();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            quizTopicItem = getIntent().getSerializableExtra("quizTopicItem", QuizTopicItem.class);
        } else {
            quizTopicItem = (QuizTopicItem) getIntent().getSerializableExtra("quizTopicItem");
        }
        progressDialog = DialogUtils.buildProgressDialog(this, "Please Wait!", "Processing...", false);
        generalQuizItems = new ArrayList<>();
        counter = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        setUI();
        fetchList();

        binding.toolbarQuiz.setNavigationOnClickListener(v -> finish());
        binding.mbOneQuiz.setOnClickListener(v -> onOptionSelection(v, 0));
        binding.mbTwoQuiz.setOnClickListener(v -> onOptionSelection(v, 1));
        binding.mbThreeQuiz.setOnClickListener(v -> onOptionSelection(v, 2));
        binding.mbFourQuiz.setOnClickListener(v -> onOptionSelection(v, 3));
        binding.mbNextQuiz.setOnClickListener(v -> onNextClick());
    }


    int previewCounter =  0;
    private void setUI() {
        binding.toolbarQuiz.setTitle(quizTopicItem.getTopicName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
                showToast(Quiz.this, error.getMessage());
            }
        };
        Query query = FirebaseUtils.dr_general_quiz.orderByChild(GeneralQuizItem_Key.TID).equalTo(quizTopicItem.getTid());
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fillList(@NonNull DataSnapshot snapshot) {
        isFetched = true;
        generalQuizItems.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            GeneralQuizItem item = dataSnapshot.getValue(GeneralQuizItem.class);
            if (item != null && Objects.equals(item.getQuizType(), SharedPreferenceUtils.getQuizLevel(this))) {
                generalQuizItems.add(item);
            }
        }
        progressDialog.dismiss();
        startSpeakingList();
        if (generalQuizItems.isEmpty()) {
            showToast(this, "No Quiz Found");
            finish();
        } else {
            initTimer();
            updateUI();
            if(PREVIEW){
                Log.i(TAG, "setUI: Called=>PREVIEW MODE & previewCount:"+previewCounter);
                int correctOption = attemptQuiz.get(previewCounter).correct;
                int selected = attemptQuiz.get(previewCounter).selected;
                if (attemptQuiz.get(previewCounter).correct == attemptQuiz.get(previewCounter).selected) {
                    switch (correctOption) {
                        case 0: {
                            binding.mbOneQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                            binding.mbThreeQuiz.setBackgroundColor(0);
                            binding.mbFourQuiz.setBackgroundColor(0);
                            binding.mbTwoQuiz.setBackgroundColor(0);
                            break;
                        }
                        case 1: {
                            binding.mbTwoQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                            binding.mbThreeQuiz.setBackgroundColor(0);
                            binding.mbFourQuiz.setBackgroundColor(0);
                            binding.mbOneQuiz.setBackgroundColor(0);
                            break;
                        }
                        case 2: {
                            binding.mbThreeQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                            binding.mbOneQuiz.setBackgroundColor(0);
                            binding.mbFourQuiz.setBackgroundColor(0);
                            binding.mbTwoQuiz.setBackgroundColor(0);
                            break;
                        }
                        case 3: {
                            binding.mbFourQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                            binding.mbThreeQuiz.setBackgroundColor(0);
                            binding.mbOneQuiz.setBackgroundColor(0);
                            binding.mbTwoQuiz.setBackgroundColor(0);
                            break;
                        }
                    }
                } else {

                    switch (correctOption) {
                        case 0: {
                            binding.mbOneQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                            binding.mbThreeQuiz.setBackgroundColor(0);
                            binding.mbFourQuiz.setBackgroundColor(0);
                            binding.mbTwoQuiz.setBackgroundColor(0);
                            break;
                        }
                        case 1: {
                            binding.mbTwoQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                            binding.mbThreeQuiz.setBackgroundColor(0);
                            binding.mbFourQuiz.setBackgroundColor(0);
                            binding.mbOneQuiz.setBackgroundColor(0);
                            break;
                        }
                        case 2: {
                            binding.mbThreeQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                            binding.mbOneQuiz.setBackgroundColor(0);
                            binding.mbFourQuiz.setBackgroundColor(0);
                            binding.mbTwoQuiz.setBackgroundColor(0);
                            break;
                        }
                        case 3: {
                            binding.mbFourQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                            binding.mbThreeQuiz.setBackgroundColor(0);
                            binding.mbOneQuiz.setBackgroundColor(0);
                            binding.mbTwoQuiz.setBackgroundColor(0);
                            break;
                        }
                    }

                    switch (selected) {
                        case 0: {

                            binding.mbOneQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
                            if(correctOption!=1){
                                binding.mbTwoQuiz.setBackgroundColor(0);
                            }
                            if(correctOption!=2){
                                binding.mbThreeQuiz.setBackgroundColor(0);

                            }
                            if(correctOption!=3){
                                binding.mbFourQuiz.setBackgroundColor(0);
                            }
                            break;
                        }
                        case 1: {
                            binding.mbTwoQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
                            if(correctOption!=0){
                                binding.mbOneQuiz.setBackgroundColor(0);
                            }
                            if(correctOption!=2){
                                binding.mbThreeQuiz.setBackgroundColor(0);

                            }
                            if(correctOption!=3){
                                binding.mbFourQuiz.setBackgroundColor(0);
                            }
                            break;
                        }
                        case 2: {
                            binding.mbThreeQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
                            if(correctOption!=1){
                                binding.mbTwoQuiz.setBackgroundColor(0);
                            }
                            if(correctOption!=0){
                                binding.mbOneQuiz.setBackgroundColor(0);

                            }
                            if(correctOption!=3){
                                binding.mbFourQuiz.setBackgroundColor(0);
                            }
                            break;
                        }
                        case 3: {
                            binding.mbFourQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
                            if(correctOption!=1){
                                binding.mbTwoQuiz.setBackgroundColor(0);
                            }
                            if(correctOption!=2){
                                binding.mbThreeQuiz.setBackgroundColor(0);

                            }
                            if(correctOption!=0){
                                binding.mbOneQuiz.setBackgroundColor(0);
                            }
                            break;
                        }
                    }
                }
                previewCounter++;
                buttonEnable(false);
            }

        }
    }

    private void initTimer() {
        long totalTimeInMillis = generalQuizItems.size() * 30L * 1000L;
        new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                String time = String.format(Locale.getDefault(), "%02d:%02d", secondsRemaining / 60, secondsRemaining % 60);
                binding.mtvTimerQuiz.setText(time);
            }

            @Override
            public void onFinish() {
                counter = generalQuizItems.size() + 1;
                onNextClick();
            }
        }.start();
    }


    private void updateUI() {
        int c = PREVIEW? previewCounter:counter;
        GeneralQuizItem item = generalQuizItems.get(c);
        binding.mtvHeadingQuiz.setText(getString(R.string._1_5, (c + 1), generalQuizItems.size()));
        binding.mtvTitleQuiz.setText(getString(R.string.question_no_1, (c + 1)));
        binding.mtvQuestionQuiz.setText(item.getQuestion());
        binding.mbOneQuiz.setText("A) " + item.getOptionA());
        binding.mbTwoQuiz.setText("B) " + item.getOptionB());
        binding.mbThreeQuiz.setText("C) " + item.getOptionC());
        binding.mbFourQuiz.setText("D) " + item.getOptionD());
        if(PREVIEW){
            if(previewCounter+1==generalQuizItems.size()){
                binding.mbNextQuiz.setText("Exit");
                binding.mbNextQuiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previewCounter = 0;
                        PREVIEW = false;
                        Quiz.this.finish();
                    }
                });
            }
        }
    }

    class AttemptQuiz{
        int correct,selected,no;

        public AttemptQuiz(int correct, int selected, int no) {
            this.correct = correct;
            this.selected = selected;
            this.no = no;
        }
    }
    private static List<AttemptQuiz> attemptQuiz = new ArrayList<>();
    private void onOptionSelection(View view, int selectionOption) {

        buttonEnable(false);
        int correctOption = generalQuizItems.get(counter).getCorrectOption();
        attemptQuiz.add(new AttemptQuiz(correctOption,selectionOption,counter+1));
                    view.setBackgroundColor(ContextCompat.getColor(this, R.color.lite_sky));


//        if (selectionOption == correctOption) {
//            generalQuizItems.get(counter).setPass(true);
//            view.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
//        } else {
//            view.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
//            switch (correctOption) {
//                case 0: {
//                    binding.mbOneQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
//                    break;
//                }
//                case 1: {
//                    binding.mbTwoQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
//                    break;
//                }
//                case 2: {
//                    binding.mbThreeQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
//                    break;
//                }
//                case 3: {
//                    binding.mbFourQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
//                    break;
//                }
//            }
//        }
        if (VoiceManager.isVoiceMode(this)) {
            onNextClick();
        }
    }

    private void buttonEnable(boolean enableButton) {
        binding.mbOneQuiz.setEnabled(enableButton);
        binding.mbTwoQuiz.setEnabled(enableButton);
        binding.mbThreeQuiz.setEnabled(enableButton);
        binding.mbFourQuiz.setEnabled(enableButton);
        binding.mbNextQuiz.setEnabled(!enableButton);
        if (enableButton) {
            binding.mbOneQuiz.setBackgroundColor(0);
            binding.mbTwoQuiz.setBackgroundColor(0);
            binding.mbThreeQuiz.setBackgroundColor(0);
            binding.mbFourQuiz.setBackgroundColor(0);
        }
    }

    private void onNextClick() {
        if(PREVIEW){
            int correctOption = attemptQuiz.get(previewCounter).correct;
            int selected = attemptQuiz.get(previewCounter).selected;
            if (attemptQuiz.get(previewCounter).correct == attemptQuiz.get(previewCounter).selected) {
                switch (correctOption) {
                    case 0: {
                        binding.mbOneQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                        binding.mbThreeQuiz.setBackgroundColor(0);
                        binding.mbFourQuiz.setBackgroundColor(0);
                        binding.mbTwoQuiz.setBackgroundColor(0);
                        break;
                    }
                    case 1: {
                        binding.mbTwoQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                        binding.mbThreeQuiz.setBackgroundColor(0);
                        binding.mbFourQuiz.setBackgroundColor(0);
                        binding.mbOneQuiz.setBackgroundColor(0);
                        break;
                    }
                    case 2: {
                        binding.mbThreeQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                        binding.mbOneQuiz.setBackgroundColor(0);
                        binding.mbFourQuiz.setBackgroundColor(0);
                        binding.mbTwoQuiz.setBackgroundColor(0);
                        break;
                    }
                    case 3: {
                        binding.mbFourQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                        binding.mbThreeQuiz.setBackgroundColor(0);
                        binding.mbOneQuiz.setBackgroundColor(0);
                        binding.mbTwoQuiz.setBackgroundColor(0);
                        break;
                    }
                }
            } else {
                switch (correctOption) {
                    case 0: {
                        binding.mbOneQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                        binding.mbThreeQuiz.setBackgroundColor(0);
                        binding.mbFourQuiz.setBackgroundColor(0);
                        binding.mbTwoQuiz.setBackgroundColor(0);
                        break;
                    }
                    case 1: {
                        binding.mbTwoQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                        binding.mbThreeQuiz.setBackgroundColor(0);
                        binding.mbFourQuiz.setBackgroundColor(0);
                        binding.mbOneQuiz.setBackgroundColor(0);
                        break;
                    }
                    case 2: {
                        binding.mbThreeQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                        binding.mbOneQuiz.setBackgroundColor(0);
                        binding.mbFourQuiz.setBackgroundColor(0);
                        binding.mbTwoQuiz.setBackgroundColor(0);
                        break;
                    }
                    case 3: {
                        binding.mbFourQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_correct));
                        binding.mbThreeQuiz.setBackgroundColor(0);
                        binding.mbOneQuiz.setBackgroundColor(0);
                        binding.mbTwoQuiz.setBackgroundColor(0);
                        break;
                    }
                }
                switch (selected) {
                    case 0: {

                        binding.mbOneQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
                        if(correctOption!=1){
                            binding.mbTwoQuiz.setBackgroundColor(0);
                        }
                        if(correctOption!=2){
                            binding.mbThreeQuiz.setBackgroundColor(0);

                        }
                        if(correctOption!=3){
                            binding.mbFourQuiz.setBackgroundColor(0);
                        }
                        break;
                    }
                    case 1: {
                        binding.mbTwoQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
                        if(correctOption!=0){
                            binding.mbOneQuiz.setBackgroundColor(0);
                        }
                        if(correctOption!=2){
                            binding.mbThreeQuiz.setBackgroundColor(0);

                        }
                        if(correctOption!=3){
                            binding.mbFourQuiz.setBackgroundColor(0);
                        }
                        break;
                    }
                    case 2: {
                        binding.mbThreeQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
                        if(correctOption!=1){
                            binding.mbTwoQuiz.setBackgroundColor(0);
                        }
                        if(correctOption!=0){
                            binding.mbOneQuiz.setBackgroundColor(0);

                        }
                        if(correctOption!=3){
                            binding.mbFourQuiz.setBackgroundColor(0);
                        }
                        break;
                    }
                    case 3: {
                        binding.mbFourQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_wrong));
                        if(correctOption!=1){
                            binding.mbTwoQuiz.setBackgroundColor(0);
                        }
                        if(correctOption!=2){
                            binding.mbThreeQuiz.setBackgroundColor(0);

                        }
                        if(correctOption!=0){
                            binding.mbOneQuiz.setBackgroundColor(0);
                        }
                        break;
                    }
                }
            }
            previewCounter++;

            if (previewCounter >= generalQuizItems.size()) {
                speakResult();
                showResult();
                return;
            }
            buttonEnable(false);
            updateUI();
        }else{
            counter++;
            if (counter >= generalQuizItems.size()) {
                speakResult();
                showResult();
                return;
            }
            updateUI();
            buttonEnable(true);
        }


    }

    private void speakResult() {
        isError = true;
        if (!VoiceManager.isVoiceMode(this)) {
            return;
        }

        int scores = getCorrectAnswers();
        float size = generalQuizItems.size();
        int percentage = Math.round(((scores / size) * 100));

        StringBuilder sb = new StringBuilder();
        sb.append("You have answered ").append(generalQuizItems.size()).append(" questions").append(", ");
        sb.append(scores).append(" of them are correct answers").append(", ");
        sb.append("Your total percentage is ").append(percentage).append(", ");
        sb.append("Say Exit quiz to finish quiz").append(", ");
        sb.append("Say Replay quiz to replay quiz");

        /*TextSpeechUtils.textSpeech(sb.toString().trim(), () -> {
            dialogResult.dismiss();
            isError = false;
            finish();
        });*/

        TextSpeechUtils.textSpeech(sb.toString().trim(), this::launchGoogleAssistant);
    }

    private int getCorrectAnswers() {
        int scores = 0;
        for (GeneralQuizItem item : generalQuizItems) {
            if (item.isPass()) {
                scores++;
            }
        }
        return scores;
    }

    private void showResult() {

        dialogResult = new DialogResultQuiz(quizTopicItem, generalQuizItems, () -> {
            PREVIEW = false;
           recreate();
        }, new OnPreviewClickListener() {
            @Override
            public void onPreviewClick() {
                dialogResult.dismiss();
                Log.i(TAG, "showResult: all attempted");
                for(AttemptQuiz a: attemptQuiz){
                    Log.i(TAG, "\n----------------\n no:"+a.no+"\t correct:"+a.correct+"\t selected:"+a.selected);
                }
                PREVIEW = true;
                recreate();
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(() ->
                dialogResult.show(getSupportFragmentManager(), "fragment_manager"), 500);

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
        if (generalQuizItems.isEmpty()) {
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
        if (counter >= generalQuizItems.size()) return;
        GeneralQuizItem item = generalQuizItems.get(counter);
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
        } else if (text.toLowerCase().contains(" a")) {
            onOptionSelection(binding.mbOneQuiz, 0);
        } else if (text.toLowerCase().contains(" b")) {
            onOptionSelection(binding.mbTwoQuiz, 1);
        } else if (text.toLowerCase().contains(" c")) {
            onOptionSelection(binding.mbThreeQuiz, 2);
        } else if (text.toLowerCase().contains(" d")) {
            onOptionSelection(binding.mbFourQuiz, 3);
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }
}