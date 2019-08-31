package com.millsofmn.android.schoolplanner.db.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.millsofmn.android.schoolplanner.db.SchoolPlannerDatabase;
import com.millsofmn.android.schoolplanner.db.dao.MentorDao;
import com.millsofmn.android.schoolplanner.db.entity.Mentor;

import java.util.List;

public class MentorRepository {
    public static final String TAG = "MentorRepository";

    private LiveData<List<Mentor>> all;

    private MentorDao dao;

    public MentorRepository(Context context){
        SchoolPlannerDatabase db = SchoolPlannerDatabase.getInstance(context);
        dao = db.mentorDao();
        all = dao.getAll();
    }

    public void insert(Mentor entity) {
        new insertAsyncTask(dao).execute(entity);
    }

    public void delete(Mentor entity){
        new deleteAsyncTask(dao).execute(entity);
    }

    public void update(Mentor entity){
        new updateAsyncTask(dao).execute(entity);
    }

    public LiveData<Mentor> findById(int id){
        return dao.findById(id);
    }

    public LiveData<List<Mentor>> findAll() {
        return all;
    }

    public LiveData<List<Mentor>> findByCourseId(int courseId){
        return dao.findByCourseId(courseId);
    }

    private static class insertAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao asyncTaskDao;

        public insertAsyncTask(MentorDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Mentor... params){
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao asyncTaskDao;

        public deleteAsyncTask(MentorDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Mentor... params){
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao asyncTaskDao;

        public updateAsyncTask(MentorDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Mentor... params){
            asyncTaskDao.update(params[0]);
            return null;
        }
    }
}
