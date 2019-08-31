package com.millsofmn.android.schoolplanner.ui.course;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.adapter.CourseMentorListAdapter;
import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.viewmodel.CourseViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.MentorViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CourseDetailsFragment extends Fragment implements CourseMentorListAdapter.OnListener {
    private static final String TAG = "CourseDetailsFragment+++";

    private static final SimpleDateFormat fmtDate = new SimpleDateFormat("MMM d, yyyy hh:mm a", Locale.getDefault());

    private CourseViewModel courseViewModel;
    private MentorViewModel mentorViewModel;

    private Course thisCourse;

    private int courseId;
    private int termId;

    private TextView tvCourseTitle;
    private TextView tvCoruseStatus;
    private TextView tvCourseStartDate;
    private TextView tvCourseEndDate;
    private RecyclerView rvCourseMentors;
    private RecyclerView rvCourseAssmt;
    private TextView tvCourseNotes;

    public static CourseDetailsFragment newInstance(int courseId, int termId){
        CourseDetailsFragment courseDetailsFragment = new CourseDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(CourseActivity.COURSE_ID_EXTRA, courseId);
        args.putInt(CourseListActivity.TERM_ID_EXTRA, termId);

        courseDetailsFragment.setArguments(args);

        return courseDetailsFragment;
    }

    public CourseDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // required by activity to use back button
    public boolean onOptionsItemSelected(MenuItem item){
//        Log.i(TAG, "tit " + item.getItemId() + " " + termId);
//
//        Intent intent = new Intent(getContext(), CourseListActivity.class);
//        intent.putExtra(CourseListActivity.TERM_ID_EXTRA, termId);
//        startActivityForResult(intent, 0);
//        return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_details, container, false);

        courseId = getArguments().getInt(CourseActivity.COURSE_ID_EXTRA, -1);
        termId = getArguments().getInt(CourseListActivity.TERM_ID_EXTRA, -1);
        Log.i(TAG, "CourseId=" + courseId + ", TermId=" + termId);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        tvCourseTitle = view.findViewById(R.id.tv_course_title);
        tvCoruseStatus = view.findViewById(R.id.tv_course_status);
        tvCourseStartDate = view.findViewById(R.id.tv_course_start_date);
        tvCourseEndDate = view.findViewById(R.id.tv_course_end_date);
        tvCourseNotes = view.findViewById(R.id.tv_course_notes);

        rvCourseMentors = view.findViewById(R.id.rv_course_mentors);
        CourseMentorListAdapter courseMentorListAdapter = new CourseMentorListAdapter(this);
        rvCourseMentors.setAdapter(courseMentorListAdapter);
        rvCourseMentors.setLayoutManager(new LinearLayoutManager(getActivity()));

        mentorViewModel.findAll().observe(this, mentors -> courseMentorListAdapter.setData(mentors));


        rvCourseAssmt = view.findViewById(R.id.rv_course_assmt);


        Observer<Course> courseObserver = course -> {
            thisCourse = course;
            if(thisCourse != null){
                if(thisCourse.getTitle() != null && !thisCourse.getTitle().isEmpty()){
                    ((CourseActivity) getActivity()).setActionBarTitle(thisCourse.getTitle());
                    setCourseDetails(course);
                }
            }
        };

        if(courseId > -1){
            courseViewModel.findById(courseId).observe(this, courseObserver);
        }

        return view;
    }

    private void setCourseDetails(Course course){
        tvCourseTitle.setText(course.getTitle());
        tvCoruseStatus.setText(course.getStatus());
        tvCourseStartDate.setText(fmtDate.format(course.getStartDate()));
        tvCourseEndDate.setText(fmtDate.format(course.getEndDate()));
        tvCourseNotes.setText(course.getNotes());
    }

    @Override
    public void onClick(int position) {

    }
}
