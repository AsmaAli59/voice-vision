package com.fyp.voicevision.helpers.interfaces;


import com.fyp.voicevision.roomDb.models.FavItem;

public interface OnBookmarkItemClickListener {
    void onItemClick(FavItem favItem);
    void onDeleteClick(FavItem favItem);
}
