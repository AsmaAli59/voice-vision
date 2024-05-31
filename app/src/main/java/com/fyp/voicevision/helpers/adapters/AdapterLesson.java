package com.fyp.voicevision.helpers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.databinding.ListItemVocabularyBinding;
import com.fyp.voicevision.helpers.interfaces.OnLessonItemClickListener;
import com.fyp.voicevision.helpers.models.Lesson;

import java.util.List;

public class AdapterLesson extends RecyclerView.Adapter<AdapterLesson.CustomViewHolder> {

    private final List<Lesson> list;
    private final OnLessonItemClickListener itemClick;

    public AdapterLesson(List<Lesson> list, OnLessonItemClickListener itemClick) {
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemVocabularyBinding binding = ListItemVocabularyBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Lesson lesson = list.get(position);
        holder.binding.mtvTitleListItemVocabulary.setText(lesson.getTitle());
        holder.binding.getRoot().setOnClickListener(v -> itemClick.onItemClick(lesson));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemVocabularyBinding binding;

        public CustomViewHolder(@NonNull ListItemVocabularyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}