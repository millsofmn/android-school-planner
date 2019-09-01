package com.millsofmn.android.schoolplanner.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.ui.SectionsStatePagerAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class CourseActivity extends AppCompatActivity {
    public static final String TAG = "CourseActivity --";
    public static final String COURSE_ID_EXTRA = "course_id";
    public static final String COURSE_TITLE_EXTRA = "course_title";

    private SectionsStatePagerAdapter adapter;
    private ViewPager viewPager;

    private int courseId;
    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        termId = getIntent().getIntExtra(CourseListActivity.TERM_ID_EXTRA, -1);
        courseId = getIntent().getIntExtra(COURSE_ID_EXTRA, -1);
        Log.i(TAG, "CourseId=" + courseId + ", TermId=" + termId);


        setTitle(getIntent().getStringExtra(COURSE_TITLE_EXTRA));

        adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.vp_course);

        setupViewPager(viewPager);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(CourseDetailsFragment.newInstance(courseId, termId), "Course Details");
//        adapter.addFragment(CourseEditFragment.newInstance(courseId, termId), "Edit Course Details");

        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }
}
