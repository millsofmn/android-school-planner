package com.millsofmn.android.schoolplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.viewmodel.MentorViewModel;

public class MentorActivity extends AppCompatActivity {

    public static final String MENTOR_ID_EXTRA = "mentor_id";
    public static final String MENTOR_NAME_EXTRA = "mentor_name";
    public static final String MENTOR_PHONE_EXTRA = "mentor_phone";
    public static final String MENTOR_EMAIL_EXTRA = "mentor_email";

    private MentorViewModel mentorViewModel;

    private int id;
    private Mentor thisMentor;

    private EditText etMentorName;
    private EditText etEmailAddress;
    private EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);


        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        etMentorName = findViewById(R.id.et_mentor_name);
        etEmailAddress = findViewById(R.id.et_mentor_emails);
        etPhoneNumber = findViewById(R.id.et_mentor_phone);

        Button btnDelete = findViewById(R.id.btn_mentor_delete);
        btnDelete.setOnClickListener(view -> {
            if (thisMentor != null) {
                mentorViewModel.delete(thisMentor);
            }
            finish();
        });

        Button btnSave = findViewById(R.id.btn_mentor_save);
        btnSave.setOnClickListener(view -> save());

        Observer<Mentor> mentorObserver = mentor -> {
            thisMentor = mentor;
            if (mentor != null) {
                if (mentor.getName() != null && !mentor.getName().isEmpty()) {
                    etMentorName.setText(mentor.getName());
                }
                if (mentor.getEmailAddress() != null && !mentor.getEmailAddress().isEmpty()) {
                    etEmailAddress.setText(mentor.getEmailAddress());
                }
                if (mentor.getPhoneNumber() != null && !mentor.getPhoneNumber().isEmpty()) {
                    etPhoneNumber.setText(mentor.getPhoneNumber());
                }
            }
        };


        if (getIntent().hasExtra(MENTOR_ID_EXTRA)) {
            id = getIntent().getIntExtra(MENTOR_ID_EXTRA, -1);
            mentorViewModel.findById(id).observe(this, mentorObserver);
        } else {
            id = -1;
        }
    }

    private void save() {

        if (!TextUtils.isEmpty(etMentorName.getText())) {
            String name = etMentorName.getText().toString();
            String phone = etPhoneNumber.getText().toString();
            String email = etEmailAddress.getText().toString();


            Intent intent = new Intent();

            intent.putExtra(MENTOR_NAME_EXTRA, name);
            intent.putExtra(MENTOR_PHONE_EXTRA, phone);
            intent.putExtra(MENTOR_EMAIL_EXTRA, email);

            if (id > -1) {
                intent.putExtra(MENTOR_ID_EXTRA, id);
            }

            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Error Updating Mentor", Toast.LENGTH_SHORT).show();
        }
    }

}
