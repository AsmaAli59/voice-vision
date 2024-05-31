package com.fyp.voicevision.helpers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.databinding.ListItemQuizTopicBinding;
import com.fyp.voicevision.helpers.interfaces.OnQuizTopicItemClickListener;
import com.fyp.voicevision.helpers.models.QuizTopicItem;

import java.util.List;

public class AdapterQuizTopic extends RecyclerView.Adapter<AdapterQuizTopic.CustomViewHolder> {

    private final List<QuizTopicItem> list;
    private final OnQuizTopicItemClickListener itemClick;

    public AdapterQuizTopic(List<QuizTopicItem> list, OnQuizTopicItemClickListener itemClick) {
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemQuizTopicBinding binding = ListItemQuizTopicBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        QuizTopicItem item = list.get(position);
        holder.binding.mtvTitle.setText(item.getTopicName());
        holder.binding.getRoot().setOnClickListener(v -> itemClick.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemQuizTopicBinding binding;

        public CustomViewHolder(@NonNull ListItemQuizTopicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}