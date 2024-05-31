package com.fyp.voicevision.helpers.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.databinding.ListItemLevelsBinding;
import com.fyp.voicevision.helpers.interfaces.OnHomeItemClickListener;
import com.fyp.voicevision.helpers.models.HomeItem;

import java.util.List;

public class AdapterLevels extends RecyclerView.Adapter<AdapterLevels.CustomViewHolder> {

    private final List<HomeItem> list;
    private final OnHomeItemClickListener itemClick;

    public AdapterLevels(List<HomeItem> list, OnHomeItemClickListener itemClick) {
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemLevelsBinding binding = ListItemLevelsBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        HomeItem homeItem = list.get(position);
        Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), homeItem.getDrawableId());
        holder.binding.mtvTitleListItemLevels.setText(homeItem.getTitle());
        holder.binding.mtvTitleListItemLevels.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        holder.binding.mcvContainerListItemLevels.setOnClickListener(v -> itemClick.onItemClick(homeItem));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemLevelsBinding binding;

        public CustomViewHolder(@NonNull ListItemLevelsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}