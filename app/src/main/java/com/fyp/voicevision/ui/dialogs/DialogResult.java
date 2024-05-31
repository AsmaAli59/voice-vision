package com.fyp.voicevision.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fyp.voicevision.databinding.DialogResultBinding;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnReplayClickListener;
import com.fyp.voicevision.helpers.keys.User_Key;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.CourseItem;
import com.fyp.voicevision.helpers.models.CourseQuizItem;
import com.fyp.voicevision.helpers.models.QuizResult;
import com.fyp.voicevision.helpers.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DialogResult extends DialogFragment {

    private CourseItem courseItem;
    private final List<CourseQuizItem> courseQuizItemList;
    private String levelType;
    private final OnReplayClickListener onReplayClickListener;
    private final FirebaseUser firebaseUser;

    public DialogResult(CourseItem courseItem, List<CourseQuizItem> courseQuizItemList, String levelType, OnReplayClickListener onReplayClickListener) {
        this.courseItem = courseItem;
        this.courseQuizItemList = courseQuizItemList;
        this.levelType = levelType;
        this.onReplayClickListener = onReplayClickListener;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DialogResultBinding binding = DialogResultBinding.inflate(getLayoutInflater());

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(binding.getRoot());

        if (!VoiceManager.isVoiceMode(getContext())) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
        }

        int scores = getCorrectAnswers();
        float size = courseQuizItemList.size();
        int percentage = Math.round(((scores / size) * 100));

        if (percentage >= 50) {
            DatabaseReference countReference = FirebaseUtils.dr_users.child(firebaseUser.getUid()).child(User_Key.CURRENT_COURSE);
            countReference.setValue(ServerValue.increment(1));
            binding.mtvNotePercentageResult.setVisibility(View.VISIBLE);
        }

        String titleOne = "No. of Questions: " + courseQuizItemList.size();
        String titleTwo = "Correct Answers:  " + scores;
        String titleThree = "Score Percentage: " + percentage + "%";

        uploadData(titleOne, titleTwo, titleThree);

        binding.mtvTotalQuestionDialogResult.setText(titleOne);
        binding.mtvCorrectedAnswersResult.setText(titleTwo);
        binding.mtvScorePercentageResult.setText(titleThree);


        binding.mbReplayDialogResult.setOnClickListener(v -> {
            dismiss();
            onReplayClickListener.onReplayClick();
        });
        binding.mbExitDialogResult.setOnClickListener(v -> {
            dismiss();
            getActivity().finish();
        });

        return dialog;
    }

    private void uploadData(String noOfQuestions, String correctAnswers, String scores) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    QuizResult quizResult = new QuizResult(FirebaseUtils.dr_quiz_result.push().getKey(), user.getUid(), user.getFullName(), user.getEmail(), noOfQuestions, correctAnswers, scores, "Classroom", levelType, courseItem.getTitle(), System.currentTimeMillis());
                    FirebaseUtils.dr_quiz_result.child(quizResult.getQr_id()).setValue(quizResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).addListenerForSingleValueEvent(valueEventListener);
    }

    private int getCorrectAnswers() {
        int scores = 0;
        for (CourseQuizItem item : courseQuizItemList) {
            if (item.isPass()) {
                scores++;
            }
        }
        return scores;
    }
}