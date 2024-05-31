package com.fyp.voicevision.helpers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.databinding.ListItemHomeBinding;
import com.fyp.voicevision.helpers.interfaces.OnHomeItemClickListener;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.HomeItem;

import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.CustomViewHolder> {

    private final List<HomeItem> list;
    private final OnHomeItemClickListener itemClick;

    public AdapterHome(List<HomeItem> list, OnHomeItemClickListener itemClick) {
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemHomeBinding binding = ListItemHomeBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        HomeItem homeItem = list.get(position);
        holder.binding.mtvTitleListItemHome.setText(homeItem.getTitle());
        holder.binding.mtvContentListItemHome.setText(homeItem.getBody());
        holder.binding.sivImageListItemHome.setImageResource(homeItem.getDrawableId());
        holder.binding.mcvContainerListItemHome.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), homeItem.getColorId()));

        if (!VoiceManager.isVoiceMode(holder.itemView.getContext())) {
            holder.binding.mcvContainerListItemHome.setClickable(true);
            holder.binding.mcvContainerListItemHome.setOnClickListener(v -> itemClick.onItemClick(homeItem));
        } else {
            holder.binding.mcvContainerListItemHome.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemHomeBinding binding;

        public CustomViewHolder(@NonNull ListItemHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}