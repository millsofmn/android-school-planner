package com.millsofmn.android.schoolplanner.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.millsofmn.android.schoolplanner.db.entity.Mentor;

import java.util.List;

@Dao
public interface MentorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Mentor... entity);

    @Update
    int update(Mentor... entity);

    @Delete
    int delete(Mentor... entity);

    @Query("SELECT * FROM mentor")
    LiveData<List<Mentor>> getAll();

    @Query("SELECT * FROM mentor WHERE id = :id")
    LiveData<Mentor> findById(int id);

    @Query("DELETE FROM mentor")
    void deleteAll();

    @Query("SELECT mentor.* FROM mentor INNER JOIN course_mentor ON mentor.id = course_mentor.mentor_id WHERE course_mentor.course_id = :courseId")
    LiveData<List<Mentor>> findByCourseId(int courseId);
}
