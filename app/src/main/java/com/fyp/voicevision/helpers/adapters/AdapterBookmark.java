package com.fyp.voicevision.helpers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.databinding.ListItemBookmarkBinding;
import com.fyp.voicevision.helpers.dataProviders.DpVocabulary;
import com.fyp.voicevision.helpers.interfaces.OnBookmarkItemClickListener;
import com.fyp.voicevision.helpers.models.VocabularyEachItem;
import com.fyp.voicevision.roomDb.models.FavItem;

import java.util.List;

public class AdapterBookmark extends RecyclerView.Adapter<AdapterBookmark.CustomViewHolder> {

    private final DpVocabulary dpVocabulary;
    private final List<FavItem> list;
    private final OnBookmarkItemClickListener itemClick;

    public AdapterBookmark(List<FavItem> list, OnBookmarkItemClickListener itemClick) {
        this.list = list;
        this.itemClick = itemClick;
        this.dpVocabulary = new DpVocabulary();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemBookmarkBinding binding = ListItemBookmarkBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        FavItem favItem = list.get(position);
        VocabularyEachItem vocabularyEachItem = dpVocabulary.getEachVocabularyList(favItem.getVocabularyId()).get(favItem.getVocabularyEachId());
        holder.binding.mtvTitleListItemBookmark.setText(vocabularyEachItem.getTitleEnglish());
        holder.binding.mtvTitleUrduListItemBookmark.setText(vocabularyEachItem.getTitleUrdu());
        holder.binding.sivIconListItemBookmark.setOnClickListener(v -> itemClick.onDeleteClick(favItem));
        holder.binding.mcvContainerListItemBookmark.setOnClickListener(v -> itemClick.onItemClick(favItem));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemBookmarkBinding binding;

        public CustomViewHolder(@NonNull ListItemBookmarkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}