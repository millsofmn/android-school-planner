package com.millsofmn.android.schoolplanner.ui.term;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.adapter.TermListAdapter;
import com.millsofmn.android.schoolplanner.db.entity.Term;
import com.millsofmn.android.schoolplanner.viewmodel.TermViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

public class TermListActivity extends AppCompatActivity  implements TermListAdapter.OnTermListener {

    public static final int ADD_MENTOR_REQUEST = 1;
    public static final int EDIT_MENTOR_REQUEST = 2;

    private TermListAdapter termListAdapter;
    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
//            Intent intent = new Intent(this, TermDetailsActivity.class);
//            startActivityForResult(intent, ADD_MENTOR_REQUEST);
        });

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        RecyclerView recyclerViewTerms = findViewById(R.id.rv_terms);

        termListAdapter = new TermListAdapter(this);
        termViewModel.getTerms().observe(this, terms -> termListAdapter.setData(terms));

        recyclerViewTerms.setAdapter(termListAdapter);
        recyclerViewTerms.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onTermClick(int position) {
//        Intent intent = new Intent(this, TermDetailsActivity.class);
//        intent.putExtra(TermDetailsActivity.MENTOR_ID_EXTRA, termListAdapter.getSelectedTerm(position).getId());
//        startActivityForResult(intent, EDIT_MENTOR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if((requestCode == EDIT_MENTOR_REQUEST || requestCode == ADD_MENTOR_REQUEST) && resultCode == RESULT_OK){

            String msg;

//            Term term = new Term();
//            term.setName(data.getStringExtra(TermDetailsActivity.MENTOR_NAME_EXTRA));
//            term.setPhoneNumber(data.getStringExtra(TermDetailsActivity.MENTOR_PHONE_EXTRA));
//            term.setEmailAddress(data.getStringExtra(TermDetailsActivity.MENTOR_EMAIL_EXTRA));
//
//            int id = data.getIntExtra(TermDetailsActivity.MENTOR_ID_EXTRA, -1);
//
//            if(id > -1){
//                term.setId(id);
//                termViewModel.update(term);
//                msg = "Term " + term.getName() + " Updated";
//            } else {
//                termViewModel.insert(term);
//                msg = "Term " + term.getName() + " Created";
//            }

            msg = "not working";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
