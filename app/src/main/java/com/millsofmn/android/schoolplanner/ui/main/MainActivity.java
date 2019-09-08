package com.millsofmn.android.schoolplanner.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.db.entity.Assessment;
import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.db.entity.CourseMentor;
import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.db.entity.Term;
import com.millsofmn.android.schoolplanner.ui.mentor.MentorListActivity;
import com.millsofmn.android.schoolplanner.ui.term.TermListActivity;
import com.millsofmn.android.schoolplanner.viewmodel.AssessmentViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.CourseMentorViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.CourseViewModel;
import com.millsofmn.android.schoolplanner.db.DatabaseSeedViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.MentorViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.TermViewModel;

import java.util.Observable;

public class MainActivity extends AppCompatActivity {

    private AssessmentViewModel assessmentViewModel;
    private CourseMentorViewModel courseMentorViewModel;
    private CourseViewModel courseViewModel;
    private MentorViewModel mentorViewModel;
    private TermViewModel termViewModel;

    private Button btnNavMentors;
    private Button btmNavMangeTerm;
    private Button btnDeleteDb;
    private Button btnPopulateDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        courseMentorViewModel = ViewModelProviders.of(this).get(CourseMentorViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        btnNavMentors = findViewById(R.id.btn_nav_mentor);
        btmNavMangeTerm = findViewById(R.id.btn_nav_terms);
        btnPopulateDb = findViewById(R.id.btn_populate_db);
        btnDeleteDb = findViewById(R.id.btn_delete_db);

        btnNavMentors.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MentorListActivity.class);
            startActivity(intent);
        });

        btmNavMangeTerm.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TermListActivity.class);
            startActivity(intent);
        });

        btnPopulateDb.setOnClickListener(view -> populateDb());

        btnDeleteDb.setOnClickListener(view -> deleteDb());
    }

    private void deleteDb(){
        courseMentorViewModel.deleteAll();
        mentorViewModel.deleteAll();
        courseViewModel.deleteAll();
        termViewModel.deleteAll();

        Toast.makeText(getApplicationContext(), "Database Deleted", Toast.LENGTH_SHORT).show();
    }

    private void populateDb(){
        for (Term term : DatabaseSeedViewModel.getTerms()) {
            termViewModel.insert(term);
        }

        for (Course course : DatabaseSeedViewModel.getCourses()) {
            courseViewModel.insert(course);
        }

        for (Assessment assessment : DatabaseSeedViewModel.getAssessments()) {
            assessmentViewModel.insert(assessment);
        }

        for (Mentor mentor : DatabaseSeedViewModel.getMentors()) {
            mentorViewModel.insert(mentor);
        }

        for (CourseMentor courseMentor : DatabaseSeedViewModel.getCourseMentors()) {
            courseMentorViewModel.insert(courseMentor);
        }
    }
}
