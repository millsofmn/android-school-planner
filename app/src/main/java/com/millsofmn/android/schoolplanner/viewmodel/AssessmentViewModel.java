package com.millsofmn.android.schoolplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.millsofmn.android.schoolplanner.db.entity.Assessment;
import com.millsofmn.android.schoolplanner.db.repository.AssessmentRepository;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private final AssessmentRepository repository;

    private LiveData<List<Assessment>> allData;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);

        repository = new AssessmentRepository(application);
        allData = repository.findAll();
    }

    public void insert(Assessment entity){
        repository.insert(entity);
    }

    public void update(Assessment entity){
        repository.update(entity);
    }

    public void delete(Assessment entity){
        repository.delete(entity);
    }

    public LiveData<List<Assessment>> findAll(){
        return allData;
    }


    public LiveData<List<Assessment>> findByCourseId(int courseId){
        return repository.findByCourseId(courseId);
    }

    public LiveData<Assessment> findById(int assmtId) {
        return repository.findById(assmtId);
    }
}