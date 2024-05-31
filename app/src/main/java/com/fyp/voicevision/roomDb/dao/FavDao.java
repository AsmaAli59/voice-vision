package com.fyp.voicevision.roomDb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fyp.voicevision.roomDb.models.FavItem;

import java.util.List;

@androidx.room.Dao
public interface FavDao {

    @Insert
    void insert(FavItem model);

    @Update
    void update(FavItem model);

    @Delete
    void delete(FavItem model);

    @Query("DELETE FROM fav_table")
    void deleteAllFav();

    @Query("SELECT * FROM fav_table ORDER BY id DESC")
    LiveData<List<FavItem>> getAllFav();
}
