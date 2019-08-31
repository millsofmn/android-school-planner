package com.millsofmn.android.schoolplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.db.repository.MentorRepository;

import java.util.List;

public class MentorViewModel extends AndroidViewModel {

    private final MentorRepository repository;

    private LiveData<List<Mentor>> allData;

    public MentorViewModel(@NonNull Application application) {
        super(application);

        repository = new MentorRepository(application);
        allData = repository.findAll();
    }

    public void insert(Mentor entity){
        repository.insert(entity);
    }

    public void update(Mentor entity){
        repository.update(entity);
    }

    public void delete(Mentor entity){
        repository.delete(entity);
    }

    public LiveData<List<Mentor>> findAll(){
        return allData;
    }

    public LiveData<Mentor> findById(int id){
        return repository.findById(id);
    }

    public LiveData<List<Mentor>> findByCourseId(int courseId){
        return repository.findByCourseId(courseId);
    }
}
