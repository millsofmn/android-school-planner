package com.millsofmn.android.schoolplanner.db.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.millsofmn.android.schoolplanner.db.SchoolPlannerDatabase;
import com.millsofmn.android.schoolplanner.db.dao.AssessmentDao;
import com.millsofmn.android.schoolplanner.db.entity.Assessment;

import java.util.List;

public class AssessmentRepository {
    public static final String TAG = "AssessmentRepository";

    private LiveData<List<Assessment>> all;

    private AssessmentDao dao;

    public AssessmentRepository(Context context){
        SchoolPlannerDatabase db = SchoolPlannerDatabase.getInstance(context);
        dao = db.assessmentDao();
        all = dao.getAll();
    }

    public void insert(Assessment entity) {
        new insertAsyncTask(dao).execute(entity);
    }

    public void delete(Assessment entity){
        new deleteAsyncTask(dao).execute(entity);
    }

    public void update(Assessment entity){
        new updateAsyncTask(dao).execute(entity);
    }

    public Assessment findById(int id){
        return dao.findById(id);
    }

    public LiveData<List<Assessment>> findAll() {
        return all;
    }

    public LiveData<List<Assessment>> findByCourseId(int courseId){
        return dao.findAllByCourseId(courseId);
    }

    private static class insertAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao asyncTaskDao;

        public insertAsyncTask(AssessmentDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Assessment... params){
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao asyncTaskDao;

        public deleteAsyncTask(AssessmentDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Assessment... params){
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao asyncTaskDao;

        public updateAsyncTask(AssessmentDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Assessment... params){
            asyncTaskDao.update(params[0]);
            return null;
        }
    }
}
