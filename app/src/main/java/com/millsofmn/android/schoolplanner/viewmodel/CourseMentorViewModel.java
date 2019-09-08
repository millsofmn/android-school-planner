package com.millsofmn.android.schoolplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.millsofmn.android.schoolplanner.db.entity.CourseMentor;
import com.millsofmn.android.schoolplanner.db.repository.CourseMentorRepository;

import java.util.List;

public class CourseMentorViewModel extends AndroidViewModel {

    private final CourseMentorRepository repository;

    private LiveData<List<CourseMentor>> allData;

    public CourseMentorViewModel(@NonNull Application application) {
        super(application);

        repository = new CourseMentorRepository(application);
        allData = repository.findAll();
    }

    public void insert(CourseMentor entity){
        repository.insert(entity);
    }

    public void update(CourseMentor entity){
        repository.update(entity);
    }

    public void delete(CourseMentor entity){
        repository.delete(entity);
    }

    public LiveData<List<CourseMentor>> findAll(){
        return allData;
    }


    public LiveData<List<CourseMentor>> findByCourseId(int courseId){
        return repository.findByCourseId(courseId);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}