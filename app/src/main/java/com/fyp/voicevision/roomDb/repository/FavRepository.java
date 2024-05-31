package com.fyp.voicevision.roomDb.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.fyp.voicevision.roomDb.dao.FavDao;
import com.fyp.voicevision.roomDb.database.FavDatabase;
import com.fyp.voicevision.roomDb.models.FavItem;

import java.util.List;

public class FavRepository {

    private FavDao dao;
    private LiveData<List<FavItem>> allFavs;

    public FavRepository(Application application) {
        FavDatabase database = FavDatabase.getInstance(application);
        dao = database.Dao();
        allFavs = dao.getAllFav();
    }

    public void insert(FavItem model) {
        new InsertFavAsyncTask(dao).execute(model);
    }

    public void update(FavItem model) {
        new UpdateFavAsyncTask(dao).execute(model);
    }

    public void delete(FavItem model) {
        new DeleteFavAsyncTask(dao).execute(model);
    }

    public void deleteAllFav() {
        new DeleteAllFavsAsyncTask(dao).execute();
    }

    public LiveData<List<FavItem>> getAllFav() {
        return allFavs;
    }

    private static class InsertFavAsyncTask extends AsyncTask<FavItem, Void, Void> {
        private FavDao dao;

        private InsertFavAsyncTask(FavDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FavItem... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }

    // we are creating a async task method to update our Fav.
    private static class UpdateFavAsyncTask extends AsyncTask<FavItem, Void, Void> {
        private FavDao dao;

        private UpdateFavAsyncTask(FavDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FavItem... models) {
            // below line is use to update
            // our modal in dao.
            dao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete Fav.
    private static class DeleteFavAsyncTask extends AsyncTask<FavItem, Void, Void> {
        private FavDao dao;

        private DeleteFavAsyncTask(FavDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FavItem... models) {
            // below line is use to delete
            // our Fav modal in dao.
            dao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all Favs.
    private static class DeleteAllFavsAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavDao dao;

        private DeleteAllFavsAsyncTask(FavDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all Favs.
            dao.deleteAllFav();
            return null;
        }
    }
}

