package com.millsofmn.android.schoolplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.millsofmn.android.schoolplanner.ui.course.CourseActivity;
import com.millsofmn.android.schoolplanner.ui.course.CourseListActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CourseEditActivity extends AppCompatActivity {
    public static final String TAG = "Edit Course++++++";
    private static final SimpleDateFormat fmtDate = new SimpleDateFormat("EEEE, MMM d, yyyy");
    private static final SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm a", Locale.getDefault());


    private EditText etCourseTitle;
    private Spinner spCourseStatus;
    private Button btnCourseStartDate;
    private Button btnCourseStartTime;
    private CheckBox cbCourseAlertOnStart;
    private Button btnCourseEndDate;
    private Button btnCourseEndTime;
    private CheckBox cbCourseAlertOnEnd;

    private Button lastButtonClicked;

    private int courseId;
    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        courseId = getIntent().getIntExtra(CourseActivity.COURSE_ID_EXTRA, -1);
        termId = getIntent().getIntExtra(CourseListActivity.TERM_ID_EXTRA, -1);
        Log.i(TAG, "CourseId=" + courseId + ", TermId=" + termId);

        etCourseTitle = findViewById(R.id.et_course_title);
        spCourseStatus = findViewById(R.id.sp_course_status);

        btnCourseStartDate = findViewById(R.id.btn_course_start_date);
        btnCourseStartDate.setOnClickListener(v -> showDatePickerDialog(btnCourseStartDate));

        btnCourseStartTime = findViewById(R.id.btn_course_start_time);
        btnCourseStartTime.setOnClickListener(v -> showTimePickerDialog(btnCourseStartTime));

        cbCourseAlertOnStart = findViewById(R.id.cb_course_alert_start);
        btnCourseEndDate = findViewById(R.id.btn_course_end_date);
        btnCourseEndDate.setOnClickListener(v -> showDatePickerDialog(btnCourseEndDate));

        btnCourseEndTime = findViewById(R.id.btn_course_end_time);
        btnCourseEndTime.setOnClickListener(v -> showTimePickerDialog(btnCourseEndTime));

        cbCourseAlertOnEnd = findViewById(R.id.cb_course_alert_end);
    }

    private void showDatePickerDialog(Button lastButtonClicked) {
        this.lastButtonClicked = lastButtonClicked;
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

            lastButtonClicked.setText(fmtDate.format(calendar.getTime()));
        }
    };

    private void showTimePickerDialog(Button lastButtonClicked){
        this.lastButtonClicked = lastButtonClicked;
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

            lastButtonClicked.setText(fmtTime.format(calendar.getTime()));
        }
    };
}
