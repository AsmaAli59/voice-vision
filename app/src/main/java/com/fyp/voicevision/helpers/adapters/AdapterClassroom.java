package com.fyp.voicevision.helpers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ListItemClassroomBinding;
import com.fyp.voicevision.helpers.interfaces.OnCourseItemClickListener;
import com.fyp.voicevision.helpers.models.CourseItem;

import java.util.List;

public class AdapterClassroom extends RecyclerView.Adapter<AdapterClassroom.CustomViewHolder> {

    private final List<CourseItem> list;
    private final OnCourseItemClickListener itemClick;

    public AdapterClassroom(List<CourseItem> list, OnCourseItemClickListener itemClick) {
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemClassroomBinding binding = ListItemClassroomBinding.inflate(layoutInflater, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        CourseItem item = list.get(position);
        if (item.isLocked())
            holder.binding.sivIconListItemClassroom.setImageResource(R.drawable.ic_password);
        else
            holder.binding.sivIconListItemClassroom.setImageResource(R.drawable.ic_next);
        holder.binding.mtvTitleListItemClassroom.setText(item.getTitle());
        holder.binding.mcvContainerListItemClassroom.setOnClickListener(v -> itemClick.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemClassroomBinding binding;

        public CustomViewHolder(@NonNull ListItemClassroomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}