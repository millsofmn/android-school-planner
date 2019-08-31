package com.millsofmn.android.schoolplanner.db.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.millsofmn.android.schoolplanner.db.SchoolPlannerDatabase;
import com.millsofmn.android.schoolplanner.db.dao.TermDao;
import com.millsofmn.android.schoolplanner.db.entity.Term;

import java.util.List;

public class TermRepository {
    public static final String TAG = "TermRepository";

    private LiveData<List<Term>> all;

    private TermDao dao;

    public TermRepository(Context context){
        SchoolPlannerDatabase db = SchoolPlannerDatabase.getInstance(context);
        dao = db.termDao();
        all = dao.getAll();
    }

    public void insert(Term entity) {
        new insertAsyncTask(dao).execute(entity);
    }

    public void delete(Term entity){
        new deleteAsyncTask(dao).execute(entity);
    }

    public void update(Term entity){
        new updateAsyncTask(dao).execute(entity);
    }

    public LiveData<Term> findById(int id){
        return dao.findById(id);
    }

    public LiveData<List<Term>> findAll() {
        return all;
    }

    private static class insertAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao asyncTaskDao;

        public insertAsyncTask(TermDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Term... params){
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao asyncTaskDao;

        public deleteAsyncTask(TermDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Term... params){
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao asyncTaskDao;

        public updateAsyncTask(TermDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Term... params){
            asyncTaskDao.update(params[0]);
            return null;
        }
    }
}
