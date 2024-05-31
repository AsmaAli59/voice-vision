package com.fyp.voicevision.roomDb.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fyp.voicevision.roomDb.dao.FavDao;
import com.fyp.voicevision.roomDb.models.FavItem;

@Database(entities = {FavItem.class}, version = 1, exportSchema = false)
public abstract class FavDatabase extends RoomDatabase {

    private static FavDatabase instance;

    public abstract FavDao Dao();

    public static synchronized FavDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FavDatabase.class, "db_voice_vision")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
