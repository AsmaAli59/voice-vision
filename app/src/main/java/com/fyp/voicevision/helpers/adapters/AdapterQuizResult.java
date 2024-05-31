package com.fyp.voicevision.helpers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ListItemQuizBinding;
import com.fyp.voicevision.helpers.enums.LevelType;
import com.fyp.voicevision.helpers.models.QuizResult;
import com.fyp.voicevision.helpers.utils.GeneralUtils;

import java.util.List;

public class AdapterQuizResult extends RecyclerView.Adapter<AdapterQuizResult.CustomViewHolder> {

    private final List<QuizResult> list;

    public AdapterQuizResult(List<QuizResult> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemQuizBinding binding = ListItemQuizBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        QuizResult quizResult = list.get(position);

        if (quizResult.getLevelType().equals(LevelType.basic.toString()))
            holder.binding.ifvIconListItemQuiz.setImageResource(R.drawable.img_quiz_basic);

        if (quizResult.getLevelType().equals(LevelType.intermediate.toString()))
            holder.binding.ifvIconListItemQuiz.setImageResource(R.drawable.img_quiz_intermediate);

        if (quizResult.getLevelType().equals(LevelType.advance.toString()))
            holder.binding.ifvIconListItemQuiz.setImageResource(R.drawable.img_quiz_advance);

        String date = "Attempt date: " + GeneralUtils.dateFormat(quizResult.getCreatedDate());
        holder.binding.mtvQuizContentListItemUser.setText(quizResult.getQuizContent());
        holder.binding.mtvQuizTypeListItemUser.setText(quizResult.getLevelType());
        holder.binding.mtvCorrectAnswerListItemUser.setText(quizResult.getCorrectAnswers());
        holder.binding.mtvPercentageListItemUser.setText(quizResult.getScoresPercentage());
        holder.binding.mtvDateListItemUser.setText(date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemQuizBinding binding;

        public CustomViewHolder(@NonNull ListItemQuizBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}