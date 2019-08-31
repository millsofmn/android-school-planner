package com.millsofmn.android.schoolplanner.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.millsofmn.android.schoolplanner.db.entity.Assessment;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Assessment... entity);

    @Update
    int update(Assessment... entity);

    @Delete
    int delete(Assessment... entity);

    @Query("SELECT * FROM assessment WHERE course_id = :courseId")
    LiveData<List<Assessment>> findAllByCourseId(int courseId);

    @Query("SELECT * FROM assessment")
    LiveData<List<Assessment>> getAll();

    @Query("SELECT * FROM assessment WHERE id = :id")
    LiveData<Assessment> findById(int id);
}
