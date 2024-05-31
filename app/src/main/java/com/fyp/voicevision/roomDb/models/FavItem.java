package com.fyp.voicevision.roomDb.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_table")
public class FavItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int vocabularyId;
    private int vocabularyEachId;

    public FavItem(int id, int vocabularyId, int vocabularyEachId) {
        this.id = id;
        this.vocabularyId = vocabularyId;
        this.vocabularyEachId = vocabularyEachId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVocabularyId() {
        return vocabularyId;
    }

    public void setVocabularyId(int vocabularyId) {
        this.vocabularyId = vocabularyId;
    }

    public int getVocabularyEachId() {
        return vocabularyEachId;
    }

    public void setVocabularyEachId(int vocabularyEachId) {
        this.vocabularyEachId = vocabularyEachId;
    }
}