package com.millsofmn.android.schoolplanner.ui.term;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.db.entity.Term;
import com.millsofmn.android.schoolplanner.viewmodel.CourseViewModel;
import com.millsofmn.android.schoolplanner.viewmodel.TermViewModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TermDetailsActivity extends AppCompatActivity {
    public static final String TAG = "TERM Activity";
    public static final String TERM_ID_EXTRA = "term_id";


    private static final String DATE_FORMAT = "EEEE, MMM d, yyyy";
    private static final String TIME_FORMAT = "h:mm a";
    private static final SimpleDateFormat fmtDate = new SimpleDateFormat(DATE_FORMAT);
    private static final SimpleDateFormat fmtTime = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
    private static final DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern(DATE_FORMAT + " " + TIME_FORMAT);

    private ConstraintLayout clTermLayout;
    private EditText etTermTitle;
    private TextView tvTermStartDate;
    private TextView tvTermStartTime;
    private TextView tvTermEndDate;
    private TextView tvTermEndTime;

    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;

    private int termId;
    private Term thisTerm;

    private TextView lastClicked;
    private List<Course> courseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        etTermTitle = findViewById(R.id.et_term_title);

        tvTermStartDate = findViewById(R.id.tv_term_start_date);
        tvTermStartTime = findViewById(R.id.tv_term_start_time);

        tvTermEndDate = findViewById(R.id.tv_term_end_date);
        tvTermEndTime = findViewById(R.id.tv_term_end_time);


        tvTermStartDate.setOnClickListener(v -> showDatePickerDialog(tvTermStartDate));
        tvTermStartTime.setOnClickListener(v -> showTimePickerDialog(tvTermStartTime));

        tvTermEndDate.setOnClickListener(v -> showDatePickerDialog(tvTermEndDate));
        tvTermEndTime.setOnClickListener(v -> showTimePickerDialog(tvTermEndTime));

        termId = getIntent().getIntExtra(TERM_ID_EXTRA, -1);

        if(termId > -1){
            termViewModel.findById(termId).observe(this, term -> setTerm(term));

            courseViewModel.getCoursesByTermId(termId).observe(this, courses -> {
                courseList.clear();
                courseList.addAll(courses);
            });
        }
    }

    private void setTerm(Term term) {
        etTermTitle.setText(term.getTitle());

        if(term.getStartDate() != null) {
            tvTermStartDate.setText(fmtDate.format(term.getStartDate()));
            tvTermStartTime.setText(fmtTime.format(term.getStartDate()));
        }

        if(term.getEndDate() != null) {
            tvTermEndDate.setText(fmtDate.format(term.getEndDate()));
            tvTermEndTime.setText(fmtTime.format(term.getEndDate()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_term_save:
                saveTerm();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_term_details, menu);
        return true;
    }

    // Persistence
    private boolean saveTerm() {
        if (isValid()) {
            Log.i(TAG, "Saving ..........");

            try {
                Term newTerm = new Term();

                newTerm.setTitle(etTermTitle.getText().toString());

                if (!TextUtils.isEmpty(tvTermStartDate.getText())) {
                    String timeString;
                    if (TextUtils.isEmpty(tvTermStartTime.getText())) {
                        timeString = "12:10 AM";
                    } else {
                        timeString = tvTermStartTime.getText().toString();
                    }
                    String startString = tvTermStartDate.getText().toString() + " " + timeString;

                    LocalDateTime startDateTime = LocalDateTime.parse(startString, fmtDateTime);
                    newTerm.setStartDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                }

                if (!TextUtils.isEmpty(tvTermEndDate.getText())) {
                    String timeString;
                    if (TextUtils.isEmpty(tvTermEndTime.getText())) {
                        timeString = "12:12 AM";
                    } else {
                        timeString = tvTermEndTime.getText().toString();
                    }
                    String endString = tvTermEndDate.getText().toString() + " " + timeString;

                    LocalDateTime endDateTime = LocalDateTime.parse(endString, fmtDateTime);
                    newTerm.setEndDate(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                }

                if (termId > -1) {
                    Log.i(TAG, "Updating ... " + termId);
                    newTerm.setId(termId);
                    termViewModel.update(newTerm);
                } else {
                    Log.i(TAG, "Inserting ... ");
                    termViewModel.insert(newTerm);
                    termId = newTerm.getId();
                    Log.i(TAG, "Inserted ... " + termId);
                }
                return true;
            } catch (Exception e) {
                Log.i(TAG, "Error parsing " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isValid() {
        return true; // todo
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
