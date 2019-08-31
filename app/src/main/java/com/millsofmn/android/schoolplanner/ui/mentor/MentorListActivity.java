package com.millsofmn.android.schoolplanner.ui.mentor;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.adapter.MentorListAdapter;
import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.viewmodel.MentorViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

public class MentorListActivity extends AppCompatActivity implements MentorListAdapter.OnMentorListener {

    public static final int ADD_MENTOR_REQUEST = 1;
    public static final int EDIT_MENTOR_REQUEST = 2;

    private MentorListAdapter mentorListAdapter;
    private MentorViewModel mentorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
                Intent intent = new Intent(this, MentorDetailsActivity.class);
                startActivityForResult(intent, ADD_MENTOR_REQUEST);
            });

        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        RecyclerView recyclerViewMentors = findViewById(R.id.rv_mentors);

        mentorListAdapter = new MentorListAdapter(this);
        mentorViewModel.findAll().observe(this, mentors -> mentorListAdapter.setData(mentors));

        recyclerViewMentors.setAdapter(mentorListAdapter);
        recyclerViewMentors.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onMentorClick(int position) {
        Intent intent = new Intent(this, MentorDetailsActivity.class);
        intent.putExtra(MentorDetailsActivity.MENTOR_ID_EXTRA, mentorListAdapter.getSelectedMentor(position).getId());
        startActivityForResult(intent, EDIT_MENTOR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if((requestCode == EDIT_MENTOR_REQUEST || requestCode == ADD_MENTOR_REQUEST) && resultCode == RESULT_OK){

            String msg;

            Mentor mentor = new Mentor();
            mentor.setName(data.getStringExtra(MentorDetailsActivity.MENTOR_NAME_EXTRA));
            mentor.setPhoneNumber(data.getStringExtra(MentorDetailsActivity.MENTOR_PHONE_EXTRA));
            mentor.setEmailAddress(data.getStringExtra(MentorDetailsActivity.MENTOR_EMAIL_EXTRA));

            int id = data.getIntExtra(MentorDetailsActivity.MENTOR_ID_EXTRA, -1);

            if(id > -1){
                mentor.setId(id);
                mentorViewModel.update(mentor);
                msg = "Mentor " + mentor.getName() + " Updated";
            } else {
                mentorViewModel.insert(mentor);
                msg = "Mentor " + mentor.getName() + " Created";
            }

            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
