package com.millsofmn.android.schoolplanner.ui.course;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.millsofmn.android.schoolplanner.MyReceiver;
import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.adapter.CourseAssmtListAdapter;
import com.millsofmn.android.schoolplanner.adapter.CourseMentorListAdapter;
import com.millsofmn.android.schoolplanner.db.entity.Assessment;
import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.db.entity.CourseMentor;
import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.ui.main.MainActivity;
import com.millsofmn.android.schoolplanner.viewmodel.AssessmentViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.CourseMentorViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.CourseViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.MentorViewModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class CourseDetailsActivity extends AppCompatActivity implements CourseMentorListAdapter.OnListener, CourseAssmtListAdapter.OnListener {
    private static final String TAG = "CourseDetailsActivity+++";

    public static final int START_ALARM_REQUEST_CODE = 123;
    public static final int END_ALARM_REQUEST_CODE = 456;

    private static final String DATE_FORMAT = "EEEE, MMM d, yyyy";
    private static final String TIME_FORMAT = "h:mm a";
    private static final SimpleDateFormat fmtDate = new SimpleDateFormat(DATE_FORMAT);
    private static final SimpleDateFormat fmtTime = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
    private static final DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern(DATE_FORMAT + " " + TIME_FORMAT);
    private static final String COURSE_MESSAGE = "course_message";

    private AssessmentViewModel assessmentViewModel;
    private CourseMentorViewModel courseMentorViewModel;
    private CourseViewModel courseViewModel;
    private MentorViewModel mentorViewModel;

    private Course thisCourse;

    private int courseId;
    private int termId;

    private ConstraintLayout constraintLayout;
    private TextView tvCourseTitle;
    private EditText etCourseTitle;
    private TextView tvCourseStatus;
    private TextView tvCourseStartDate;
    private TextView tvCourseStartTime;
    private CheckBox cbCourseAlertOnStart;
    private TextView tvCourseEndDate;
    private TextView tvCourseEndTime;
    private CheckBox cbCourseAlertOnEnd;
    private TextView tvCourseLblMentors;
    private RecyclerView rvCourseMentors;
    private TextView tvCourseLblAddMentors;
    private ImageButton ibCourseAddAssmt;
    private RecyclerView rvCourseAssmt;
    private TextView tvCourseNotes;
    private EditText etCourseNotes;
    private ImageButton ibCourseShareNote;

    private CourseAssmtListAdapter courseAssmtListAdapter;
    private CourseMentorListAdapter courseMentorListAdapter;

    private Observer<List<Mentor>> mentorObserver;
    private Observer<List<CourseMentor>> courseMentorObserver;
    private Observer<Course> courseObserver;

    private TextView lastClicked;
    private List<Mentor> allMentorsList = new ArrayList<>();
    private List<Mentor> selectedMentors = new ArrayList<>();
    private List<CourseMentor> courseMentors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        constraintLayout = findViewById(R.id.cl_course_layout);

        // View Models
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        courseMentorViewModel = ViewModelProviders.of(this).get(CourseMentorViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        // Ui
        tvCourseTitle = findViewById(R.id.tv_course_title);
        etCourseTitle = findViewById(R.id.et_course_title);

        tvCourseStatus = findViewById(R.id.sp_course_status);
        tvCourseStartDate = findViewById(R.id.btn_course_start_date);
        tvCourseStartTime = findViewById(R.id.tv_course_start_time);
        cbCourseAlertOnStart = findViewById(R.id.cb_course_alert_on_start);

        tvCourseEndDate = findViewById(R.id.btn_course_end_date);
        tvCourseEndTime = findViewById(R.id.tv_course_end_time);
        cbCourseAlertOnEnd = findViewById(R.id.cb_course_alert_on_end);

        tvCourseNotes = findViewById(R.id.tv_course_notes);
        etCourseNotes = findViewById(R.id.et_course_notes);

        tvCourseLblMentors = findViewById(R.id.tv_course_lbl_mentors);
        tvCourseLblMentors.setOnClickListener(v -> showMentorSelectionDialog());

        rvCourseMentors = findViewById(R.id.rv_course_mentors);
        courseMentorListAdapter = new CourseMentorListAdapter(this);
        rvCourseMentors.setAdapter(courseMentorListAdapter);
        rvCourseMentors.setLayoutManager(new LinearLayoutManager(this));

        tvCourseLblAddMentors = findViewById(R.id.tv_course_lbl_add_mentors);
        tvCourseLblAddMentors.setOnClickListener(v -> showMentorSelectionDialog());

        rvCourseAssmt = findViewById(R.id.rv_course_assmt);
        courseAssmtListAdapter = new CourseAssmtListAdapter(this);
        rvCourseAssmt.setAdapter(courseAssmtListAdapter);
        rvCourseAssmt.setLayoutManager(new LinearLayoutManager(this));

        ibCourseAddAssmt = findViewById(R.id.ib_course_add_assmt);
        ibCourseShareNote = findViewById(R.id.ib_course_share_note);

        // Listeners
        constraintLayout.setOnClickListener(view -> {
            disableEditing();
            saveCourse();
        });

        tvCourseTitle.setOnClickListener(view -> {
            tvCourseTitle.setVisibility(View.GONE);
            etCourseTitle.setVisibility(View.VISIBLE);
        });

        tvCourseStatus.setOnClickListener(v -> showCourseStatusDialog());

        tvCourseStartDate.setOnClickListener(v -> showDatePickerDialog(tvCourseStartDate));
        tvCourseStartTime.setOnClickListener(v -> showTimePickerDialog(tvCourseStartTime));

        tvCourseEndDate.setOnClickListener(v -> showDatePickerDialog(tvCourseEndDate));
        tvCourseEndTime.setOnClickListener(v -> showTimePickerDialog(tvCourseEndTime));

        ibCourseAddAssmt.setOnClickListener(v -> createNewCourseAssessment());
        ibCourseShareNote.setOnClickListener(v -> shareNote());

        tvCourseNotes.setOnClickListener(view -> {
            tvCourseNotes.setVisibility(View.GONE);
            etCourseNotes.setVisibility(View.VISIBLE);
        });

        // Observers
        mentorObserver = m -> allMentorsList.addAll(m);
        courseMentorObserver = cms -> courseMentors.addAll(cms);
        courseObserver = course -> {
            thisCourse = course;
            if (course != null) {
                setCourseDetails(course);
            }
        };

        // Preload
        mentorViewModel.findAll().observe(this, mentorObserver);

        if (getIncomingIntent()) {
            loadCourseDetails();
        } else {
            setNewCourse();
        }
    }

    private void shareNote() {
        String title = tvCourseTitle.getText().toString();
        String note = tvCourseNotes.getText().toString();

        String msg = "Here are my notes of the course " + title + ":\n" + note;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        startActivity(intent);
    }

    private void setNewCourse() {
        tvCourseLblMentors.setOnClickListener(null);
        rvCourseMentors.setVisibility(View.INVISIBLE);

        tvCourseLblAddMentors.setVisibility(View.INVISIBLE);

        rvCourseAssmt.setVisibility(View.INVISIBLE);
        ibCourseAddAssmt.setVisibility(View.INVISIBLE);

        tvCourseTitle.setVisibility(View.GONE);
        etCourseTitle.setVisibility(View.VISIBLE);

        tvCourseNotes.setVisibility(View.GONE);
        etCourseNotes.setVisibility(View.VISIBLE);
    }

    private boolean getIncomingIntent() {

        if (!getIntent().hasExtra(CourseListActivity.TERM_ID_EXTRA)) {
            throw new RuntimeException("I don't know how you got here but it's bad!");
        }

        termId = getIntent().getIntExtra(CourseListActivity.TERM_ID_EXTRA, -1);

        courseId = getIntent().getIntExtra(CourseListActivity.COURSE_ID_EXTRA, -1);
        Log.i(TAG, "CourseId=" + courseId + ", TermId=" + termId);

        if (courseId > -1) {
            return true;
        }
        return false;
    }

    private void loadCourseDetails() {
        courseViewModel.findById(courseId).observe(this, courseObserver);

        assessmentViewModel.findByCourseId(courseId).observe(this,
                assessments -> courseAssmtListAdapter.setData(assessments));

        mentorViewModel.findByCourseId(courseId).observe(this, mentors -> {
            selectedMentors.addAll(mentors);
            courseMentorListAdapter.setData(mentors);
            if (mentors.isEmpty()) {
                tvCourseLblAddMentors.setVisibility(View.VISIBLE);
            } else {
                tvCourseLblAddMentors.setVisibility(View.GONE);
            }
        });

        courseMentorViewModel.findByCourseId(courseId).observe(this, courseMentorObserver);
    }

    private void setCourseDetails(Course course) {
        setTitle("Course Details");
        tvCourseTitle.setText(course.getTitle());
        tvCourseTitle.setVisibility(View.VISIBLE);
        etCourseTitle.setText(course.getTitle());
        etCourseTitle.setVisibility(View.GONE);

        tvCourseStatus.setText(course.getStatus());

        if(course.getStartDate() != null) {
            tvCourseStartDate.setText(fmtDate.format(course.getStartDate()));
            tvCourseStartTime.setText(fmtTime.format(course.getStartDate()));
        }
        cbCourseAlertOnStart.setChecked(course.isAlertOnStartDate());

        if(course.getEndDate() != null) {
            tvCourseEndDate.setText(fmtDate.format(course.getEndDate()));
            tvCourseEndTime.setText(fmtTime.format(course.getEndDate()));
        }
        cbCourseAlertOnEnd.setChecked(course.isAlertOnEndDate());

        tvCourseNotes.setText(course.getNotes());
        tvCourseNotes.setVisibility(View.VISIBLE);
        etCourseNotes.setText(course.getNotes());
        etCourseNotes.setVisibility(View.GONE);

    }

    private void disableEditing() {

        tvCourseTitle.setText(etCourseTitle.getText());
        tvCourseNotes.setText(etCourseNotes.getText());

        etCourseTitle.setVisibility(View.GONE);
        tvCourseTitle.setVisibility(View.VISIBLE);

        tvCourseNotes.setVisibility(View.VISIBLE);
        etCourseNotes.setVisibility(View.GONE);

        supportInvalidateOptionsMenu();
    }

    // MENU
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
//            case R.id.item_course_edit:
//                disableEditing();
//                return true;
            case R.id.item_course_save:
                if (saveCourse()) finish();
                return true;
            default:
                saveCourse();
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isValid() {
        boolean valid = true;

        if (TextUtils.isEmpty(etCourseTitle.getText())) {
            etCourseTitle.setError("Course title is required!");
            valid = false;
        } else {
            etCourseTitle.setError(null);
        }


        return valid;
    }

    // Persistence
    private boolean saveCourse() {
        if (isValid()) {
            Log.i(TAG, "Saving ..........");

            try {
                Course thisCourse = new Course();

                thisCourse.setTermId(termId);

                thisCourse.setTitle(etCourseTitle.getText().toString());
                thisCourse.setStatus(tvCourseStatus.getText().toString());

                if (!TextUtils.isEmpty(tvCourseStartDate.getText())) {
                    String timeString;
                    if (TextUtils.isEmpty(tvCourseStartTime.getText())) {
                        timeString = "12:10 AM";
                    } else {
                        timeString = tvCourseStartTime.getText().toString();
                    }
                    String startString = tvCourseStartDate.getText().toString() + " " + timeString;

                    LocalDateTime startDateTime = LocalDateTime.parse(startString, fmtDateTime);
                    thisCourse.setStartDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    thisCourse.setAlertOnStartDate(cbCourseAlertOnStart.isChecked());

                    if(cbCourseAlertOnStart.isChecked() && LocalDateTime.now().isBefore(startDateTime)){
                        scheduleAlarm(startDateTime, "Your course " + thisCourse.getTitle() + " has started.", START_ALARM_REQUEST_CODE);
                    }
                }

                if (!TextUtils.isEmpty(tvCourseEndDate.getText())) {
                    String timeString;
                    if (TextUtils.isEmpty(tvCourseEndTime.getText())) {
                        timeString = "12:12 AM";
                    } else {
                        timeString = tvCourseEndTime.getText().toString();
                    }
                    String endString = tvCourseEndDate.getText().toString() + " " + timeString;

                    LocalDateTime endDateTime = LocalDateTime.parse(endString, fmtDateTime);
                    thisCourse.setEndDate(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    thisCourse.setAlertOnEndDate(cbCourseAlertOnEnd.isChecked());

                    if(cbCourseAlertOnEnd.isChecked() && LocalDateTime.now().isBefore(endDateTime)){
                        Log.i(TAG, "Adding another one =======");
                        scheduleAlarm(endDateTime, "Your course " + thisCourse.getTitle() + " has ended.", END_ALARM_REQUEST_CODE);
                    }
                }

                thisCourse.setNotes(etCourseNotes.getText().toString());


                if (courseId > -1) {
                    Log.i(TAG, "Updating ... " + courseId);
                    thisCourse.setId(courseId);
                    courseViewModel.update(thisCourse);
                } else {
                    Log.i(TAG, "Inserting ... ");
                    courseViewModel.insert(thisCourse);
                    courseId = thisCourse.getId();
                    Log.i(TAG, "Inserted ... " + courseId);
                }
                disableEditing();
                return true;
            } catch (Exception e) {
                Log.i(TAG, "Error parsing " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    private void scheduleAlarm(LocalDateTime time, String msg, int requestCode) {
        Log.i(TAG, "Scheduling course alert ............ : " + msg);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra(MyReceiver.ALERT_TITLE, "Course Action");
        intent.putExtra(MyReceiver.ALERT_MESSAGE, msg);
        intent.putExtra(CourseListActivity.TERM_ID_EXTRA, termId);
        intent.putExtra(CourseListActivity.COURSE_ID_EXTRA, courseId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), pendingIntent);

    }

    private void saveMentors() {
        for (CourseMentor cm : courseMentors) {
            courseMentorViewModel.delete(cm);
        }

        for (Mentor mentor : selectedMentors) {
            CourseMentor courseMentor = new CourseMentor(courseId, mentor.getId());
            courseMentorViewModel.insert(courseMentor);
        }
    }


    private void showDeleteCourseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete Course?");
        builder.setPositiveButton("Delete", (dialogInterface, i) -> {
            if (thisCourse != null) {
                for (CourseMentor cm : courseMentors) {
                    courseMentorViewModel.delete(cm);
                }
                courseViewModel.delete(thisCourse);
            }
            finish();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // click
        });
        builder.show();
    }

    // STATUS DIALOG
    private void showCourseStatusDialog() {
        String[] courseStatuses = getResources().getStringArray(R.array.course_statuses);
        int idx = IntStream.range(0, courseStatuses.length)
                .filter(i -> tvCourseStatus.getText().equals(courseStatuses[i]))
                .findFirst()
                .orElse(-1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Set Course Status")
                .setSingleChoiceItems(courseStatuses, idx, (dialogInterface, i) -> {
                    tvCourseStatus.setText(courseStatuses[i]);
                    dialogInterface.dismiss();
                });
        builder.show();
    }

    // MENTOR DIALOG

    @Override
    public void onCourseMentorClick(int position) {
        showMentorSelectionDialog();
    }

    @Override
    public void onCourseAssessmentClick(int position) {
        Assessment selectedAssessment = courseAssmtListAdapter.getSelectedItem(position);

        Intent intent = new Intent(this, AssessmentActivity.class);
        intent.putExtra(CourseListActivity.COURSE_ID_EXTRA, courseId);
        intent.putExtra(AssessmentActivity.ASSMT_ID_SELECTED, selectedAssessment.getId());
        startActivityForResult(intent, AssessmentActivity.EDIT_ASSMT_REQUEST);
    }

    private void createNewCourseAssessment() {
        Intent intent = new Intent(this, AssessmentActivity.class);
        intent.putExtra(CourseListActivity.COURSE_ID_EXTRA, courseId);
        startActivityForResult(intent, AssessmentActivity.EDIT_ASSMT_REQUEST);
    }

    private void showMentorSelectionDialog() {
        List<Integer> selectedMentorsIndexList = new ArrayList<>();

        String[] them = new String[allMentorsList.size()];
        boolean[] themChecked = new boolean[allMentorsList.size()];

        for (int i = 0; i < allMentorsList.size(); i++) {
            Mentor mentor = allMentorsList.get(i);
            them[i] = mentor.getName();

            Mentor checked = selectedMentors.stream().filter(mentor1 -> mentor.getId() == mentor1.getId()).findAny().orElse(null);

            if (checked != null) {
                themChecked[i] = true;
                selectedMentorsIndexList.add(i);
            } else {
                themChecked[i] = false;
            }
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Select Mentors for Class")
                .setMultiChoiceItems(them, themChecked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                                if (isChecked) {
                                    selectedMentorsIndexList.add(which);
                                } else if (selectedMentorsIndexList.contains(which)) {
                                    selectedMentorsIndexList.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedMentors.clear();

                        for (int x : selectedMentorsIndexList) {
                            Mentor mentor = allMentorsList.get(x);
                            selectedMentors.add(mentor);
                            Log.i(TAG, "Selected " + mentor.getName());
                        }
                        saveMentors();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }


    // DATE PICKER
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

    private void showTimePickerDialog(TextView lastClicked) {
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
