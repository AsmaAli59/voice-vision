package com.fyp.voicevision.helpers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.databinding.ListItemVocabularyBinding;
import com.fyp.voicevision.helpers.interfaces.OnVocabularyItemClickListener;
import com.fyp.voicevision.helpers.models.VocabularyItem;

import java.util.List;

public class AdapterVocabulary extends RecyclerView.Adapter<AdapterVocabulary.CustomViewHolder> {

    private final List<VocabularyItem> list;
    private final OnVocabularyItemClickListener itemClick;

    public AdapterVocabulary(List<VocabularyItem> list, OnVocabularyItemClickListener itemClick) {
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
        VocabularyItem item = list.get(position);
        holder.binding.mtvTitleListItemVocabulary.setText(item.getTitle());
        holder.binding.mcvContainerListItemVocabulary.setOnClickListener(v -> itemClick.onItemClick(item));
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