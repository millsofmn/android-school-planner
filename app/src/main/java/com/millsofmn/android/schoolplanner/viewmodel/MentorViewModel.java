package com.millsofmn.android.schoolplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.db.repository.MentorRepository;

import java.util.List;

public class MentorViewModel extends AndroidViewModel {

    private final MentorRepository mentorRepository;

    private LiveData<List<Mentor>> mentors;

    public MentorViewModel(@NonNull Application application) {
        super(application);

        mentorRepository = new MentorRepository(application);
        mentors = mentorRepository.findAll();
    }

    public void insert(Mentor entity){
        mentorRepository.insert(entity);
    }

    public void update(Mentor entity){
        mentorRepository.update(entity);
    }

    public void delete(Mentor entity){
        mentorRepository.delete(entity);
    }

    public LiveData<List<Mentor>> getMentors(){
        return mentors;
    }

    public LiveData<Mentor> findById(int id){
        return mentorRepository.findById(id);
    }
}
