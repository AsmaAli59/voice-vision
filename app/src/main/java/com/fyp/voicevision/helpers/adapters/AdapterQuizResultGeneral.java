package com.fyp.voicevision.helpers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ListItemQuizGeneralBinding;
import com.fyp.voicevision.helpers.enums.LevelType;
import com.fyp.voicevision.helpers.models.QuizResult;
import com.fyp.voicevision.helpers.utils.GeneralUtils;

import java.util.List;

public class AdapterQuizResultGeneral extends RecyclerView.Adapter<AdapterQuizResultGeneral.CustomViewHolder> {

    private final List<QuizResult> list;

    public AdapterQuizResultGeneral(List<QuizResult> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemQuizGeneralBinding binding = ListItemQuizGeneralBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        QuizResult quizResult = list.get(position);

        String quizType = "Quiz Type: " + quizResult.getQuizContent();
        String date = "Attempt date: " + GeneralUtils.dateFormat(quizResult.getCreatedDate());

        if (quizResult.getLevelType().equals(LevelType.basic.toString()))
            holder.binding.ifvIconListItemQuizGeneral.setImageResource(R.drawable.img_quiz_basic);

        if (quizResult.getLevelType().equals(LevelType.intermediate.toString()))
            holder.binding.ifvIconListItemQuizGeneral.setImageResource(R.drawable.img_quiz_intermediate);

        if (quizResult.getLevelType().equals(LevelType.advance.toString()))
            holder.binding.ifvIconListItemQuizGeneral.setImageResource(R.drawable.img_quiz_advance);

        holder.binding.mtvQuizTypeListItemUserGeneral.setText(quizType);
        holder.binding.mtvCorrectAnswerListItemUserGeneral.setText(quizResult.getCorrectAnswers());
        holder.binding.mtvNumberOfQuestionListItemUserGeneral.setText(quizResult.getNoOfQuestions());
        holder.binding.mtvPercentageListItemUserGeneral.setText(quizResult.getScoresPercentage());
        holder.binding.mtvDateListItemUser.setText(date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemQuizGeneralBinding binding;

        public CustomViewHolder(@NonNull ListItemQuizGeneralBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}