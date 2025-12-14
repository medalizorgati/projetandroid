package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class offres extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int jdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offres);

        dbHelper = new DatabaseHelper(this);
        jdId = getIntent().getIntExtra("JD_ID", -1); // Get the logged-in user's ID

        TextView backButton = findViewById(R.id.btnRetour);
        backButton.setOnClickListener(v -> finish());

        // Get all offers from the database
        ArrayList<Offre> aOffres = (ArrayList<Offre>) dbHelper.getAllOffres();

        // Create the adapter to convert the array to views
        OffreAdapter adapter = new OffreAdapter(this, aOffres);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listOffres);
        listView.setAdapter(adapter);
        
        // Pass both the offer ID and the user ID to the description screen
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Offre selectedOffre = aOffres.get(position);
            Intent intent = new Intent(this, OffreDescriptionActivity.class);
            intent.putExtra("OFFRE_ID", selectedOffre.getId());
            intent.putExtra("JD_ID", jdId);
            startActivity(intent);
        });
    }
}
