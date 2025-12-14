package com.example.projetandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CandidateActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView candidatesRecyclerView;
    private CandidateAdapter adapter;
    private TextView emptyView;
    private int societeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);
        candidatesRecyclerView = findViewById(R.id.candidatesRecyclerView);
        candidatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        emptyView = findViewById(R.id.emptyView);

        societeId = getIntent().getIntExtra("SOCIETE_ID", -1);

        if (societeId == -1) {
            // Handle error
            finish();
            return;
        }

        loadCandidates();
    }

    private void loadCandidates() {
        List<Candidat> candidats = dbHelper.getCandidatsForSociete(societeId);

        if (candidats.isEmpty()) {
            candidatesRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            candidatesRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter = new CandidateAdapter(candidats);
            candidatesRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
