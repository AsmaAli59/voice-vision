package com.fyp.voicevision.helpers.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.databinding.ListItemModesBinding;
import com.fyp.voicevision.helpers.interfaces.OnHomeItemClickListener;
import com.fyp.voicevision.helpers.models.HomeItem;

import java.util.List;

public class AdapterModes extends RecyclerView.Adapter<AdapterModes.CustomViewHolder> {

    private final List<HomeItem> list;
    private final OnHomeItemClickListener itemClick;

    public AdapterModes(List<HomeItem> list, OnHomeItemClickListener itemClick) {
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemModesBinding binding = ListItemModesBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        HomeItem homeItem = list.get(position);
        Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), homeItem.getDrawableId());
        holder.binding.mtvTitleListItemModes.setText(homeItem.getTitle());
        holder.binding.mtvTitleListItemModes.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        holder.binding.mcvContainerListItemModes.setOnClickListener(v -> itemClick.onItemClick(homeItem));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemModesBinding binding;

        public CustomViewHolder(@NonNull ListItemModesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}