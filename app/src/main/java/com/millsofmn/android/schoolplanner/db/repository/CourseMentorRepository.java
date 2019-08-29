package com.millsofmn.android.schoolplanner.db.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.millsofmn.android.schoolplanner.db.SchoolPlannerDatabase;
import com.millsofmn.android.schoolplanner.db.dao.CourseMentorDao;
import com.millsofmn.android.schoolplanner.db.entity.CourseMentor;

import java.util.List;

public class CourseMentorRepository {
    public static final String TAG = "CourseMentorRepository";

    private LiveData<List<CourseMentor>> all;

    private CourseMentorDao dao;

    public CourseMentorRepository(Context context){
        SchoolPlannerDatabase db = SchoolPlannerDatabase.getInstance(context);
        dao = db.courseMentorDao();
        all = dao.getAll();
    }

    public void insert(CourseMentor entity) {
        new insertAsyncTask(dao).execute(entity);
    }

    public void delete(CourseMentor entity){
        new deleteAsyncTask(dao).execute(entity);
    }

    public void update(CourseMentor entity){
        new updateAsyncTask(dao).execute(entity);
    }

    public LiveData<List<CourseMentor>> findAll() {
        return all;
    }

    public LiveData<List<CourseMentor>> findByCourseId(int courseId){
        return dao.findByCourseId(courseId);
    }

    private static class insertAsyncTask extends AsyncTask<CourseMentor, Void, Void> {
        private CourseMentorDao asyncTaskDao;

        public insertAsyncTask(CourseMentorDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CourseMentor... params){
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<CourseMentor, Void, Void> {
        private CourseMentorDao asyncTaskDao;

        public deleteAsyncTask(CourseMentorDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CourseMentor... params){
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<CourseMentor, Void, Void> {
        private CourseMentorDao asyncTaskDao;

        public updateAsyncTask(CourseMentorDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CourseMentor... params){
            asyncTaskDao.update(params[0]);
            return null;
        }
    }
}
