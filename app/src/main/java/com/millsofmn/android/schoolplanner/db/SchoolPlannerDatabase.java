package com.millsofmn.android.schoolplanner.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.millsofmn.android.schoolplanner.db.converter.Converters;
import com.millsofmn.android.schoolplanner.db.dao.AssessmentDao;
import com.millsofmn.android.schoolplanner.db.dao.CourseDao;
import com.millsofmn.android.schoolplanner.db.dao.CourseMentorDao;
import com.millsofmn.android.schoolplanner.db.dao.MentorDao;
import com.millsofmn.android.schoolplanner.db.dao.TermDao;
import com.millsofmn.android.schoolplanner.db.entity.Assessment;
import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.db.entity.CourseMentor;
import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.db.entity.Term;

@Database(
        entities = {
                Assessment.class,
                Course.class,
                CourseMentor.class,
                Mentor.class,
                Term.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class SchoolPlannerDatabase extends RoomDatabase {
    public static final String TAG = "SchoolPlannerDatabase";

    public abstract AssessmentDao assessmentDao();

    public abstract CourseDao courseDao();

    public abstract CourseMentorDao courseMentorDao();

    public abstract MentorDao mentorDao();

    public abstract TermDao termDao();

    private static volatile SchoolPlannerDatabase INSTANCE;

    public static SchoolPlannerDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (SchoolPlannerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(
                                    context.getApplicationContext(),
                                    SchoolPlannerDatabase.class,
                                    "school_planner_database")
//                            .addCallback(schoolPlannerDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback schoolPlannerDatabaseCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase database) {
            super.onOpen(database);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final AssessmentDao assessmentDao;
        private final CourseDao courseDao;
        private final CourseMentorDao courseMentorDao;
        private final MentorDao mentorDao;
        private final TermDao termDao;

        public PopulateDbAsync(SchoolPlannerDatabase database) {
            assessmentDao = database.assessmentDao();
            courseDao = database.courseDao();
            courseMentorDao = database.courseMentorDao();
            mentorDao = database.mentorDao();
            termDao = database.termDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Log.i(TAG, "initialize database with data");
            courseMentorDao.deleteAll();
            mentorDao.deleteAll();
            courseDao.deleteAll();
            termDao.deleteAll();

            for (Term term : DatabaseSeedViewModel.getTerms()) {
                termDao.insert(term);
            }

            for (Course course : DatabaseSeedViewModel.getCourses()) {
                courseDao.insert(course);
            }

            for (Assessment assessment : DatabaseSeedViewModel.getAssessments()) {
                assessmentDao.insert(assessment);
            }

            for (Mentor mentor : DatabaseSeedViewModel.getMentors()) {
                mentorDao.insert(mentor);
            }


            for (CourseMentor courseMentor : DatabaseSeedViewModel.getCourseMentors()) {
                courseMentorDao.insert(courseMentor);
            }


            return null;
        }
    }
}
