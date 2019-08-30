package com.millsofmn.android.schoolplanner.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.ui.mentor.MentorListActivity;
import com.millsofmn.android.schoolplanner.ui.term.TermListActivity;

public class MainActivity extends AppCompatActivity {


    private Button btnNavMentors;
    private Button btmNavMangeTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNavMentors = findViewById(R.id.btn_nav_mentor);
        btmNavMangeTerm = findViewById(R.id.btn_nav_terms);

        btnNavMentors.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MentorListActivity.class);
            startActivity(intent);
        });

        btmNavMangeTerm.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TermListActivity.class);
            startActivity(intent);
        });
    }
}