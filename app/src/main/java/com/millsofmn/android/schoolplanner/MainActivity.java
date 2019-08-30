package com.millsofmn.android.schoolplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button btnNavMentors;
    private Button btmNavMangeTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNavMentors = findViewById(R.id.btn_nav_mentor);
        btmNavMangeTerm = findViewById(R.id.btn_nav_terms);

        btnNavMentors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MentorActivity.class);
                startActivity(intent);
            }
        });

        btmNavMangeTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Going to Terms", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), TermsListActivity.class);
//                startActivity(intent);
            }
        });
    }
}
