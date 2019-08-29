package com.millsofmn.android.schoolplanner.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.millsofmn.android.schoolplanner.db.entity.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course... entity);

    @Update
    int update(Course... entity);

    @Delete
    int delete(Course... entity);

    @Query("SELECT * FROM course")
    LiveData<List<Course>> getAll();

    @Query("SELECT * FROM course WHERE id = :id")
    Course findById(int id);

    @Query("SELECT * FROM course WHERE term_id = :termId")
    LiveData<List<Course>> findByTermId(int termId);

    @Query("SELECT count(id) FROM course WHERE term_id = :termId")
    Integer getCountByTermId(int termId);

    @Query("DELETE FROM course")
    void deleteAll();
}
