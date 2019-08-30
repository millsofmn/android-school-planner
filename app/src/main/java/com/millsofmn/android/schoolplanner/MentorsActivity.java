package com.millsofmn.android.schoolplanner;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.millsofmn.android.schoolplanner.adapter.MentorListAdapter;
import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.viewmodel.MentorViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

public class MentorsActivity extends AppCompatActivity implements MentorListAdapter.OnMentorListener {

    public static final int ADD_MENTOR_REQUEST = 1;
    public static final int EDIT_MENTOR_REQUEST = 2;

    private MentorListAdapter mentorListAdapter;
    private MentorViewModel mentorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
                Intent intent = new Intent(this, MentorActivity.class);
                startActivityForResult(intent, ADD_MENTOR_REQUEST);
            });

        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        RecyclerView recyclerViewMentors = findViewById(R.id.rv_mentor);

        mentorListAdapter = new MentorListAdapter(this);
        mentorViewModel.getMentors().observe(this, mentors -> mentorListAdapter.setData(mentors));

        recyclerViewMentors.setAdapter(mentorListAdapter);
        recyclerViewMentors.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onMentorClick(int position) {
        Toast.makeText(getApplicationContext(), "You clicked on a mentor", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MentorActivity.class);
        intent.putExtra(MentorActivity.MENTOR_ID_EXTRA, mentorListAdapter.getSelectedMentor(position).getId());
        startActivityForResult(intent, EDIT_MENTOR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if((requestCode == EDIT_MENTOR_REQUEST || requestCode == ADD_MENTOR_REQUEST) && resultCode == RESULT_OK){

            String msg;

            Mentor mentor = new Mentor();
            mentor.setName(data.getStringExtra(MentorActivity.MENTOR_NAME_EXTRA));
            mentor.setPhoneNumber(data.getStringExtra(MentorActivity.MENTOR_PHONE_EXTRA));
            mentor.setEmailAddress(data.getStringExtra(MentorActivity.MENTOR_EMAIL_EXTRA));

            int id = data.getIntExtra(MentorActivity.MENTOR_ID_EXTRA, -1);

            if(id > -1){
                mentor.setId(id);
                mentorViewModel.update(mentor);
                msg = "Mentor Updated";
            } else {
                mentorViewModel.insert(mentor);
                msg = "Mentor Created";
            }

            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
