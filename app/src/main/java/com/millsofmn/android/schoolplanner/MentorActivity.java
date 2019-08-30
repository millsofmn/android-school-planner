package com.millsofmn.android.schoolplanner;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.millsofmn.android.schoolplanner.adapter.MentorListAdapter;
import com.millsofmn.android.schoolplanner.viewmodel.MentorViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

public class MentorActivity extends AppCompatActivity implements MentorListAdapter.OnMentorListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        RecyclerView recyclerViewMentors = findViewById(R.id.rv_mentor);

        final MentorListAdapter mentorListAdapter = new MentorListAdapter(this);
        mentorViewModel.getMentors().observe(this, mentors -> mentorListAdapter.setData(mentors));

        recyclerViewMentors.setAdapter(mentorListAdapter);
        recyclerViewMentors.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onMentorClick(int position) {
        Toast.makeText(getApplicationContext(), "You clicked on a mentor", Toast.LENGTH_SHORT).show();

    }
}
