package com.fyp.voicevision.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fyp.voicevision.databinding.DialogResultBinding;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnPreviewClickListener;
import com.fyp.voicevision.helpers.interfaces.OnReplayClickListener;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.GeneralQuizItem;
import com.fyp.voicevision.helpers.models.QuizResult;
import com.fyp.voicevision.helpers.models.QuizTopicItem;
import com.fyp.voicevision.helpers.models.User;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.ui.activities.home.quiz.Quiz;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DialogResultQuiz extends DialogFragment {

    private DialogResultBinding binding;
    private final QuizTopicItem quizTopicItem;
    private final List<GeneralQuizItem> generalQuizItems;
    private final OnReplayClickListener onReplayClickListener;
    private final OnPreviewClickListener onPreviewClickListener;
    private final FirebaseUser firebaseUser;

    public DialogResultQuiz(QuizTopicItem quizTopicItem, List<GeneralQuizItem> generalQuizItems, OnReplayClickListener onReplayClickListener, OnPreviewClickListener onPreviewClickListener) {
        this.quizTopicItem = quizTopicItem;
        this.generalQuizItems = generalQuizItems;
        this.onReplayClickListener = onReplayClickListener;
        this.onPreviewClickListener = onPreviewClickListener;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogResultBinding.inflate(getLayoutInflater());

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(binding.getRoot());

        int scores = getCorrectAnswers();
        float size = generalQuizItems.size();
        int percentage = Math.round(((scores / size) * 100));

        String titleOne = "No. of Questions: " + generalQuizItems.size();
        String titleTwo = "Correct Answers:  " + scores;
        String titleThree = "Score Percentage: " + percentage + "%";

        uploadData(titleOne, titleTwo, titleThree);

        binding.mtvTotalQuestionDialogResult.setText(titleOne);
        binding.mtvCorrectedAnswersResult.setText(titleTwo);
        binding.mtvScorePercentageResult.setText(titleThree);

        startSpeakingList();
        binding.previewBtn.setOnClickListener(v->onPreviewClickListener.onPreviewClick());

        binding.mbReplayDialogResult.setOnClickListener(v -> {
            dismiss();
            onReplayClickListener.onReplayClick();
        });
        binding.mbExitDialogResult.setOnClickListener(v -> {
            onExitClick();
        });

        return dialog;
    }

    private void uploadData(String noOfQuestions, String correctAnswers, String scores) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    QuizResult quizResult = new QuizResult(
                            FirebaseUtils.dr_quiz_result.push().getKey(),
                            user.getUid(),
                            user.getFullName(),
                            user.getEmail(),
                            noOfQuestions,
                            correctAnswers,
                            scores,
                            "Quiz",
                            SharedPreferenceUtils.getQuizLevel(getContext()),
                            quizTopicItem.getTopicName(),
                            System.currentTimeMillis()
                    );
                    FirebaseUtils.dr_quiz_result.child(quizResult.getQr_id()).setValue(quizResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).addListenerForSingleValueEvent(valueEventListener);
    }


    private void onExitClick() {
        dismiss();
        Quiz.PREVIEW = false;
        getActivity().finish();
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

    private void startSpeakingList() {
        if (!VoiceManager.isVoiceMode(getContext())) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }

        binding.incVoice.getRoot().setVisibility(View.VISIBLE);
        binding.incVoice.mtvTextVoiceMode.setVisibility(View.GONE);

        binding.incVoice.mbExitVoiceMode.setOnClickListener(v -> VoiceManager.exitVoiceMode(getActivity()));
    }
}
