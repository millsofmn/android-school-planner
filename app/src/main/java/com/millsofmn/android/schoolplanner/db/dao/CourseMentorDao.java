package com.millsofmn.android.schoolplanner.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.millsofmn.android.schoolplanner.db.entity.CourseMentor;

import java.util.List;

@Dao
public interface CourseMentorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseMentor... entity);

    @Update
    int update(CourseMentor... entity);

    @Delete
    int delete(CourseMentor... entity);

    @Query("DELETE FROM course_mentor")
    void deleteAll();

    @Query("SELECT * FROM course_mentor")
    LiveData<List<CourseMentor>> getAll();

    @Query("SELECT * FROM course_mentor WHERE course_id = :courseId")
    LiveData<List<CourseMentor>> findByCourseId(int courseId);
}
