package com.millsofmn.android.schoolplanner.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.millsofmn.android.schoolplanner.db.entity.Term;

import java.util.List;

@Dao
public interface TermDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    int delete(Term... terms);

    @Query("DELETE FROM term")
    void deleteAll();

    @Query("SELECT * FROM term ORDER BY start_date ASC")
    LiveData<List<Term>> getAll();

    @Query("SELECT * FROM term WHERE id = :id")
    LiveData<Term> findById(int id);
}
