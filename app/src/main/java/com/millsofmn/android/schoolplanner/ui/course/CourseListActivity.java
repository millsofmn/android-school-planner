package com.millsofmn.android.schoolplanner.ui.course;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.adapter.CourseListAdapter;
import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.db.entity.Term;
import com.millsofmn.android.schoolplanner.viewmodel.CourseViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.TermViewModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class CourseListActivity extends AppCompatActivity implements CourseListAdapter.OnCourseListener {
    public static final int EDIT_COURSE_REQUEST = 1;

    public static final String TERM_ID_EXTRA = "term_id";
    public static final String TERM_TITLE_EXTRA = "term_title";

    private CourseListAdapter courseListAdapter;
    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;

    private Term thisTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        RecyclerView recyclerViewTerms = findViewById(R.id.rv_courses);

        courseListAdapter = new CourseListAdapter(this);

        int termId = getIntent().getIntExtra(TERM_ID_EXTRA, -1);

        termViewModel.findById(termId).observe(this, term -> {
            if(term != null) {
                thisTerm = term;
                setTitle(term.getTitle());
            }
        });

        courseViewModel.getCoursesByTermId(termId).observe(this, courses -> courseListAdapter.setData(courses));

        recyclerViewTerms.setAdapter(courseListAdapter);
        recyclerViewTerms.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onCourseClick(int position) {
        Intent intent = new Intent(this, CourseActivity.class);
        Course selectedCourse = courseListAdapter.getSelectedCourse(position);
        intent.putExtra(CourseListActivity.TERM_ID_EXTRA, thisTerm.getId());
        intent.putExtra(CourseActivity.COURSE_ID_EXTRA, selectedCourse.getId());

        startActivityForResult(intent, EDIT_COURSE_REQUEST);
        startActivity(intent);
    }
}
