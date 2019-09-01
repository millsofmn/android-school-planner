package com.millsofmn.android.schoolplanner.ui.course;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.adapter.CourseAssmtListAdapter;
import com.millsofmn.android.schoolplanner.adapter.CourseMentorListAdapter;
import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.viewmodel.AssessmentViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.CourseViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.MentorViewModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CourseDetailsActivity extends AppCompatActivity implements CourseMentorListAdapter.OnListener {
    private static final String TAG = "CourseDetailsActivity+++";

    private static final String DATE_FORMAT = "EEEE, MMM d, yyyy";
    private static final String TIME_FORMAT = "h:mm a";
    private static final SimpleDateFormat fmtDate = new SimpleDateFormat(DATE_FORMAT);
    private static final SimpleDateFormat fmtTime = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
    private static final DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern(DATE_FORMAT + " " + TIME_FORMAT);

    private AssessmentViewModel assessmentViewModel;
    private CourseViewModel courseViewModel;
    private MentorViewModel mentorViewModel;

    private Course thisCourse;

    private boolean courseEditable = false;

    private int courseId;
    private int termId;

    private TextView tvCourseTitle;
    private EditText etCourseTitle;
    private TextView tvCourseStatus;
    private TextView tvCourseStartDate;
    private TextView tvCourseStartTime;
    private TextView tvCourseEndDate;
    private TextView tvCourseEndTime;
    private RecyclerView rvCourseMentors;
    private RecyclerView rvCourseAssmt;
    private TextView tvCourseNotes;

    private TextView lastClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        courseId = getIntent().getIntExtra(CourseActivity.COURSE_ID_EXTRA, -1);
        termId = getIntent().getIntExtra(CourseListActivity.TERM_ID_EXTRA, -1);
        Log.i(TAG, "CourseId=" + courseId + ", TermId=" + termId);

        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        tvCourseTitle = findViewById(R.id.tv_course_title);
        etCourseTitle = findViewById(R.id.et_course_title);
        tvCourseStatus = findViewById(R.id.sp_course_status);
        tvCourseStartDate = findViewById(R.id.btn_course_start_date);
        tvCourseStartTime = findViewById(R.id.tv_course_start_time);
        tvCourseEndDate = findViewById(R.id.btn_course_end_date);
        tvCourseEndTime = findViewById(R.id.tv_course_end_time);
        tvCourseNotes = findViewById(R.id.tv_course_notes);

        rvCourseMentors = findViewById(R.id.rv_course_mentors);
        rvCourseAssmt = findViewById(R.id.rv_course_assmt);

        if (courseId > -1) {
            initForm();
            setEditable(false);
        }
    }

    private void initForm() {
        CourseMentorListAdapter courseMentorListAdapter = new CourseMentorListAdapter(this);
        rvCourseMentors.setAdapter(courseMentorListAdapter);
        rvCourseMentors.setLayoutManager(new LinearLayoutManager(this));

        mentorViewModel.findByCourseId(courseId).observe(this, mentors -> courseMentorListAdapter.setData(mentors));

        CourseAssmtListAdapter courseAssmtListAdapter = new CourseAssmtListAdapter(this);
        rvCourseAssmt.setAdapter(courseAssmtListAdapter);
        rvCourseAssmt.setLayoutManager(new LinearLayoutManager(this));

        assessmentViewModel.findByCourseId(courseId).observe(this, assessments -> courseAssmtListAdapter.setData(assessments));

        Observer<Course> courseObserver = course -> {
            thisCourse = course;
            if (thisCourse != null) {
                if (thisCourse.getTitle() != null && !thisCourse.getTitle().isEmpty()) {
                    setTitle("Course Details");
                    setCourseDetails(course);
                }
            }
        };
        courseViewModel.findById(courseId).observe(this, courseObserver);


    }

    private void setCourseDetails(Course course) {
        tvCourseTitle.setText(course.getTitle());
        etCourseTitle.setText(course.getTitle());

        tvCourseStatus.setText(course.getStatus());

        tvCourseStartDate.setText(fmtDate.format(course.getStartDate()));
        tvCourseStartTime.setText(fmtTime.format(course.getStartDate()));
        tvCourseEndDate.setText(fmtDate.format(course.getEndDate()));
        tvCourseEndTime.setText(fmtTime.format(course.getEndDate()));

        tvCourseNotes.setText(course.getNotes());

    }

    private void setEditable(boolean editable) {
        this.courseEditable = editable;

        supportInvalidateOptionsMenu();

        int tvEditable;
        int etEditable;

        if (courseEditable) {
            tvEditable = View.GONE;
            etEditable = View.VISIBLE;
        } else {
            tvEditable = View.VISIBLE;
            etEditable = View.GONE;
        }

        tvCourseTitle.setVisibility(tvEditable);
        etCourseTitle.setVisibility(etEditable);

        if (courseEditable) {
            tvCourseStatus.setOnClickListener(v -> showCourseStatusDialog());

            tvCourseStartDate.setOnClickListener(v -> showDatePickerDialog(tvCourseStartDate));
            tvCourseStartTime.setOnClickListener(v -> showTimePickerDialog(tvCourseStartTime));

            tvCourseEndDate.setOnClickListener(v -> showDatePickerDialog(tvCourseEndDate));
            tvCourseEndTime.setOnClickListener(v -> showTimePickerDialog(tvCourseEndTime));
        } else {
            tvCourseStatus.setOnClickListener(null);

            tvCourseStartDate.setOnClickListener(null);
            tvCourseStartTime.setOnClickListener(null);

            tvCourseEndDate.setOnClickListener(null);
            tvCourseEndTime.setOnClickListener(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_course_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_course_delete:
                showDeleteCourseDialog();
                return true;
            case R.id.item_course_edit:
                setEditable(true);
                return true;
            case R.id.item_course_save:
                saveCourse();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveCourse() {
        if (!TextUtils.isEmpty(etCourseTitle.getText()) && termId > -1) {

            try {
                Course course;

                if (courseId > -1) {
                    course = thisCourse;
                } else {
                    course = new Course();
                }

                thisCourse.setTitle(etCourseTitle.getText().toString());
                thisCourse.setStatus(tvCourseStatus.getText().toString());

                String startString = tvCourseStartDate.getText().toString() + " " + tvCourseStartTime.getText().toString();
                LocalDateTime startDateTime = LocalDateTime.parse(startString, fmtDateTime);
                thisCourse.setStartDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));

                String endString = tvCourseEndDate.getText().toString() + " " + tvCourseEndTime.getText().toString();
                LocalDateTime endDateTime = LocalDateTime.parse(endString, fmtDateTime);
                thisCourse.setEndDate(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));


//                Date startDate = fmtDate.parse(buttonStartDate.getText().toString());
//                Date endDate = fmtDate.parse(buttonEndDate.getText().toString());
//                String status = courseStatus.getSelectedItem().toString();
//                String notes = editTextNotes.getText().toString();
//
                courseViewModel.update(thisCourse);

            } catch (Exception e) {
                Log.i(TAG, "Error parsing " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        setEditable(false);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (courseEditable) {
            menu.findItem(R.id.item_course_save).setVisible(true);
            menu.findItem(R.id.item_course_edit).setVisible(false);
        } else {
            menu.findItem(R.id.item_course_save).setVisible(false);
            menu.findItem(R.id.item_course_edit).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(int position) {

    }

    private void showDeleteCourseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete Course?");
        builder.setPositiveButton("Delete", (dialogInterface, i) -> {
            if (thisCourse != null) {
                courseViewModel.delete(thisCourse);
            }
            finish();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // click
        });
        builder.show();
    }

    private void showCourseStatusDialog() {
        String[] courseStatuses = getResources().getStringArray(R.array.course_statuses);
        int idx = IntStream.range(0, courseStatuses.length)
                .filter(i -> tvCourseStatus.getText().equals(courseStatuses[i]))
                .findFirst()
                .orElse(-1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Set Course Status")
                .setSingleChoiceItems(courseStatuses, idx,(dialogInterface, i) -> {
                    tvCourseStatus.setText(courseStatuses[i]);
                    dialogInterface.dismiss();
                });
        builder.show();
    }

    private void showMentorSelectionDialog(){

    }
    private void showDatePickerDialog(TextView lastClicked) {
        this.lastClicked = lastClicked;
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                this,
                setDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private final DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {
        final Calendar calendar = Calendar.getInstance();
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            lastClicked.setText(fmtDate.format(calendar.getTime()));
        }
    };

    private void showTimePickerDialog(TextView lastClicked){
        this.lastClicked = lastClicked;
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(
                this,
                setTime,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false).show();
    }

    private final TimePickerDialog.OnTimeSetListener setTime = new TimePickerDialog.OnTimeSetListener() {
        final Calendar calendar = Calendar.getInstance();
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            lastClicked.setText(fmtTime.format(calendar.getTime()));
        }
    };
}
