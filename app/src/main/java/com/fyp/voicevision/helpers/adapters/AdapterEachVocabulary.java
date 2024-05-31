package com.fyp.voicevision.helpers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ListItemEachVocabularyBinding;
import com.fyp.voicevision.helpers.interfaces.OnVocabularyEachItemClickListener;
import com.fyp.voicevision.helpers.models.VocabularyEachItem;
import com.fyp.voicevision.roomDb.models.FavItem;

import java.util.List;

public class AdapterEachVocabulary extends RecyclerView.Adapter<AdapterEachVocabulary.CustomViewHolder> {

    private final List<FavItem> favItemList;
    private final List<VocabularyEachItem> list;
    private final OnVocabularyEachItemClickListener itemClick;

    public AdapterEachVocabulary(List<FavItem> favItemList, List<VocabularyEachItem> list, OnVocabularyEachItemClickListener itemClick) {
        this.favItemList = favItemList;
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemEachVocabularyBinding binding = ListItemEachVocabularyBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        VocabularyEachItem item = list.get(position);

        holder.binding.ifvFavListItemEachVocabulary.setImageResource(R.drawable.ic_un_fav);
        for (FavItem fav : favItemList) {
            if (fav.getVocabularyEachId() == item.getId() && item.getVid() == fav.getVocabularyId()) {
                holder.binding.ifvFavListItemEachVocabulary.setImageResource(R.drawable.ic_fav);
            }
        }

        holder.binding.mtvTitleListItemEachVocabulary.setText(item.getTitleEnglish());
        holder.binding.mtvTitleUrduListItemEachVocabulary.setText(item.getTitleUrdu());
        holder.binding.sivIconListItemEachVocabulary.setOnClickListener(v -> itemClick.onItemClick(item));
        holder.binding.ifvFavListItemEachVocabulary.setOnClickListener(v -> itemClick.onFavClick(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemEachVocabularyBinding binding;

        public CustomViewHolder(@NonNull ListItemEachVocabularyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}