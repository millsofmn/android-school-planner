package com.millsofmn.android.schoolplanner.db.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.millsofmn.android.schoolplanner.db.SchoolPlannerDatabase;
import com.millsofmn.android.schoolplanner.db.dao.CourseDao;
import com.millsofmn.android.schoolplanner.db.entity.Course;

import java.util.List;

public class CourseRepository {
    public static final String TAG = "CourseRepository";

    private LiveData<List<Course>> all;

    private CourseDao dao;

    public CourseRepository(Context context){
        SchoolPlannerDatabase db = SchoolPlannerDatabase.getInstance(context);
        dao = db.courseDao();
        all = dao.getAll();
    }

    public void insert(Course entity) {
        new insertAsyncTask(dao).execute(entity);
    }

    public void delete(Course entity){
        new deleteAsyncTask(dao).execute(entity);
    }

    public void update(Course entity){
        new updateAsyncTask(dao).execute(entity);
    }

    public int getCountByTermId(int termId){
        return dao.getCountByTermId(termId);
    }

    public LiveData<List<Course>> findAll() {
        return all;
    }

    public LiveData<List<Course>> findByTermId(int termId){
        return dao.findByTermId(termId);
    }

    public LiveData<Course> findById(int courseId) {
        return dao.findById(courseId);
    }

    private static class insertAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao asyncTaskDao;

        public insertAsyncTask(CourseDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Course... params){
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao asyncTaskDao;

        public deleteAsyncTask(CourseDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Course... params){
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao asyncTaskDao;

        public updateAsyncTask(CourseDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Course... params){
            asyncTaskDao.update(params[0]);
            return null;
        }
    }
}
