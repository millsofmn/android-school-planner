package com.millsofmn.android.schoolplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.db.repository.CourseRepository;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final CourseRepository repository;

    private LiveData<List<Course>> allData;

    public CourseViewModel(@NonNull Application application) {
        super(application);

        repository = new CourseRepository(application);
        allData = repository.findAll();
    }

    public void insert(Course entity){
        repository.insert(entity);
    }

    public void update(Course entity){
        repository.update(entity);
    }

    public void delete(Course entity){
        repository.delete(entity);
    }

    public LiveData<List<Course>> findAll(){
        return allData;
    }


    public LiveData<List<Course>> getCoursesByTermId(int termId){
        return repository.findByTermId(termId);
    }
}