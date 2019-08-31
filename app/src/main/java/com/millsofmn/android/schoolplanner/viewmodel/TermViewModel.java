package com.millsofmn.android.schoolplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.millsofmn.android.schoolplanner.db.entity.Term;
import com.millsofmn.android.schoolplanner.db.repository.TermRepository;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private final TermRepository repository;

    private LiveData<List<Term>> allData;

    public TermViewModel(@NonNull Application application) {
        super(application);

        repository = new TermRepository(application);
        allData = repository.findAll();
    }

    public void insert(Term entity){
        repository.insert(entity);
    }

    public void update(Term entity){
        repository.update(entity);
    }

    public void delete(Term entity){
        repository.delete(entity);
    }

    public LiveData<List<Term>> findAll(){
        return allData;
    }

    public LiveData<Term> findById(int id){
        return repository.findById(id);
    }
}
