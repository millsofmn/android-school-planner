package com.millsofmn.android.schoolplanner.ui.course;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.millsofmn.android.schoolplanner.MyReceiver;
import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.db.entity.Assessment;
import com.millsofmn.android.schoolplanner.viewmodel.AssessmentViewModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.stream.IntStream;

public class AssessmentActivity extends AppCompatActivity {
    private static final String TAG = "Assessment Activity";

    private static final String DATE_FORMAT = "EEEE, MMM d, yyyy";
    private static final String TIME_FORMAT = "h:mm a";
    private static final SimpleDateFormat fmtDate = new SimpleDateFormat(DATE_FORMAT);
    private static final SimpleDateFormat fmtTime = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
    private static final DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern(DATE_FORMAT + " " + TIME_FORMAT);

    public static final int EDIT_ASSMT_REQUEST = 1;
    public static final String COURSE_ID_EXTRA = "course_id";
    public static final String ASSMT_ID_SELECTED = "assessment_id";

    private AssessmentViewModel assessmentViewModel;

    private ConstraintLayout constraintLayout;

    private TextView tvAssmtTitle;
    private EditText etAssmtTitle;
    private TextView tvAssmtType;
    private TextView tvAssmtDate;
    private TextView tvAssmtTime;
    private CheckBox cbAssmtAlert;

    private Assessment thisAssessment;
    private int courseId;
    private int assessmentId;

    private TextView lastClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // ui
        constraintLayout = findViewById(R.id.cl_assmt_layout);
        tvAssmtTitle = findViewById(R.id.tv_assmt_title);
        etAssmtTitle = findViewById(R.id.et_assmt_title);
        tvAssmtType = findViewById(R.id.tv_assmt_type);
        tvAssmtDate = findViewById(R.id.tv_assmt_date);
        tvAssmtTime = findViewById(R.id.tv_assmt_time);
        cbAssmtAlert = findViewById(R.id.cb_assmt_alert);

        // view model
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        Observer<Assessment> assessmentObserver = assessment -> {
            if (assessment != null) {
                thisAssessment = assessment;
                setAssessment(assessment);
            }
        };

        // listeners
        constraintLayout.setOnClickListener(view -> {
            etAssmtTitle.setVisibility(View.GONE);
            tvAssmtTitle.setVisibility(View.VISIBLE);
            tvAssmtTitle.setText(etAssmtTitle.getText());

            if(assessmentId > -1){
                save();
            }
        });

        tvAssmtTitle.setOnClickListener(v -> {
            etAssmtTitle.setVisibility(View.VISIBLE);
            tvAssmtTitle.setVisibility(View.GONE);
            etAssmtTitle.setText(tvAssmtTitle.getText());
        });

        tvAssmtDate.setOnClickListener(v -> showDatePickerDialog(tvAssmtDate));
        tvAssmtTime.setOnClickListener(v -> showTimePickerDialog(tvAssmtTime));
        tvAssmtType.setOnClickListener(v -> showAssessmentTypeDialog());

        courseId = getIntent().getIntExtra(COURSE_ID_EXTRA, -1);
        assessmentId = getIntent().getIntExtra(ASSMT_ID_SELECTED, -1);

        if (assessmentId > -1) {
            assessmentViewModel.findById(assessmentId).observe(this, assessmentObserver);
        } else {
            tvAssmtTitle.setVisibility(View.GONE);
        }
    }

    private void setAssessment(Assessment assessment) {
        tvAssmtTitle.setText(assessment.getTitle());
        etAssmtTitle.setText(assessment.getTitle());
        tvAssmtType.setText(assessment.getPerformanceType());

        if (assessment.getDueDate() != null) {
            tvAssmtDate.setText(fmtDate.format(assessment.getDueDate()));
            tvAssmtTime.setText(fmtTime.format(assessment.getDueDate()));
        }
        cbAssmtAlert.setChecked(assessment.isAlertOnDueDate());

        etAssmtTitle.setVisibility(View.GONE);
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
            case R.id.item_course_save:
                if (save()) finish();
                return true;
            default:
                if (save()) {
                    return super.onOptionsItemSelected(item);
                } else {
                    showConfirmLeaveDialog();
                    return true;
                }
        }
    }

    private boolean save() {
        if (isValid()) {

            Assessment assessment = new Assessment();

            assessment.setTitle(etAssmtTitle.getText().toString());
            assessment.setCourseId(courseId);
            assessment.setPerformanceType(tvAssmtType.getText().toString());

            if (!TextUtils.isEmpty(tvAssmtDate.getText())) {

                String timeString;
                if (TextUtils.isEmpty(tvAssmtTime.getText())) {
                    timeString = "12:15 AM";
                } else {
                    timeString = tvAssmtTime.getText().toString();
                }
                String startString = tvAssmtDate.getText().toString() + " " + timeString;

                LocalDateTime startDateTime = LocalDateTime.parse(startString, fmtDateTime);
                assessment.setDueDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));

                if(cbAssmtAlert.isChecked() && LocalDateTime.now().isBefore(startDateTime)){
                    scheduleAlarm(startDateTime, "Your assessment " + assessment.getTitle() + " is due.");
                }
            }

            assessment.setAlertOnDueDate(cbAssmtAlert.isChecked());

            if (assessmentId > -1) {
                Log.i(TAG, "Updating new Assessment " + assessmentId);
                assessment.setId(assessmentId);
                assessmentViewModel.update(assessment);
            } else {
                Log.i(TAG, "Inserting new Assessment");
                assessmentViewModel.insert(assessment);
            }
            return true;

        }

        return false;
    }

    private boolean isValid() {
        boolean valid = true;

        if (TextUtils.isEmpty(etAssmtTitle.getText())) {
            etAssmtTitle.setError("Assessment title is required.");
            valid = false;
        } else {
            etAssmtTitle.setError(null);
        }

        if (TextUtils.isEmpty(tvAssmtType.getText())) {
            tvAssmtType.setError("Assessment type is required.");
            valid = false;
        } else {
            tvAssmtType.setError(null);
        }

        return valid;
    }

    private void showDeleteCourseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete assessment?");
        builder.setPositiveButton("Delete", (dialogInterface, i) -> {
            if (thisAssessment != null) {
                assessmentViewModel.delete(thisAssessment);
            }
            finish();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // click
        });
        builder.show();
    }

    private void showConfirmLeaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Assessment is invalid are you sure you want to leave?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            finish();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // click
        });
        builder.show();
    }


    // TYPE DIALOG
    private void showAssessmentTypeDialog() {
        String[] courseStatuses = getResources().getStringArray(R.array.assessment_types);
        int idx = IntStream.range(0, courseStatuses.length)
                .filter(i -> tvAssmtType.getText().equals(courseStatuses[i]))
                .findFirst()
                .orElse(-1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Set Assessment Type")
                .setSingleChoiceItems(courseStatuses, idx, (dialogInterface, i) -> {
                    tvAssmtType.setText(courseStatuses[i]);
                    dialogInterface.dismiss();
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

    private void scheduleAlarm(LocalDateTime time, String msg) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra(MyReceiver.ALERT_TITLE, "Assessment Due");
        intent.putExtra(MyReceiver.ALERT_MESSAGE, msg);
        intent.putExtra(ASSMT_ID_SELECTED, assessmentId);
        intent.putExtra(CourseListActivity.COURSE_ID_EXTRA, courseId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 852, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), pendingIntent);

    }
}
