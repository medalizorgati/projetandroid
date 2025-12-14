package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SocieteMenuActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView offresRecyclerView;
    private SocieteOffreAdapter adapter;
    private TextView emptyView;
    private int societeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_societe_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide the default title

        dbHelper = new DatabaseHelper(this);
        offresRecyclerView = findViewById(R.id.offresRecyclerView);
        offresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        emptyView = findViewById(R.id.emptyView);

        societeId = getIntent().getIntExtra("SOCIETE_ID", -1);

        if (societeId == -1) {
            // Handle error
            finish();
            return;
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_menu) {
                // Already on the menu screen, do nothing
                return true;
            } else if (itemId == R.id.navigation_ajoute) {
                Intent intent = new Intent(SocieteMenuActivity.this, AddOffreActivity.class);
                intent.putExtra("SOCIETE_ID", societeId);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_candidate) {
                Intent intent = new Intent(SocieteMenuActivity.this, CandidateActivity.class);
                intent.putExtra("SOCIETE_ID", societeId);
                startActivity(intent);
                return true;
            }
            return false;
        });

        loadOffres();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOffres();
    }

    private void loadOffres() {
        List<Offre> offres = dbHelper.getOffresBySociete(societeId);

        if (offres.isEmpty()) {
            offresRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            offresRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter = new SocieteOffreAdapter(offres);
            offresRecyclerView.setAdapter(adapter);
        }
    }
}
