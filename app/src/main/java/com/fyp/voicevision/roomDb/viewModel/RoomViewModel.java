package com.fyp.voicevision.roomDb.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fyp.voicevision.roomDb.models.FavItem;
import com.fyp.voicevision.roomDb.repository.FavRepository;

import java.util.List;

public class RoomViewModel extends AndroidViewModel {

    private FavRepository repository;

    private LiveData<List<FavItem>> allFavs;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        repository = new FavRepository(application);
        allFavs = repository.getAllFav();
    }

    public void insert(FavItem model) {
        repository.insert(model);
    }

    public void update(FavItem model) {
        repository.update(model);
    }

    public void delete(FavItem model) {
        repository.delete(model);
    }

    public void deleteAllFav() {
        repository.deleteAllFav();
    }

    public LiveData<List<FavItem>> getAllFav() {
        return allFavs;
    }
}
