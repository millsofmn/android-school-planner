package com.millsofmn.android.schoolplanner.ui.course;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.viewmodel.CourseViewModel;

public class CourseDetailsFragment extends Fragment {
    private static final String TAG = "CourseDetailsFragment+++";

    private CourseViewModel courseViewModel;
    private Course thisCourse;

    private int courseId;
    private int termId;

    private TextView tvCourseTitle;

    public static CourseDetailsFragment newInstance(int courseId){
        CourseDetailsFragment courseDetailsFragment = new CourseDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(CourseActivity.COURSE_ID_EXTRA, courseId);

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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getContext(), CourseListActivity.class);
        intent.putExtra(CourseListActivity.TERM_ID_EXTRA, termId);
        startActivityForResult(intent, 0);
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_details, container, false);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        tvCourseTitle = view.findViewById(R.id.textView33);

        courseId = getArguments().getInt(CourseActivity.COURSE_ID_EXTRA, -1);
        termId = getArguments().getInt(CourseListActivity.TERM_ID_EXTRA, -1);

        Observer<Course> courseObserver = course -> {
            thisCourse = course;
            if(thisCourse != null){
                if(thisCourse.getTitle() != null && !thisCourse.getTitle().isEmpty()){
                    tvCourseTitle.setText(thisCourse.getTitle());

                    ((CourseActivity) getActivity()).setActionBarTitle(thisCourse.getTitle());
                }
            }
        };

        if(courseId > -1){
            courseViewModel.findById(courseId).observe(this, courseObserver);
        }

        return view;
    }

}
