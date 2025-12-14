package com.example.projetandroid;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OffreDescriptionActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Offre offre;
    private int jdId; // To store the logged-in user's ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre_description);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);
        int offreId = getIntent().getIntExtra("OFFRE_ID", -1);
        jdId = getIntent().getIntExtra("JD_ID", -1);

        if (offreId == -1) {
            Toast.makeText(this, "Erreur: ID de l'offre non trouvé", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        offre = dbHelper.getOffreById(offreId);

        if (offre == null) {
            Toast.makeText(this, "Erreur: Offre non trouvée", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        populateViews();

        Button postulerBtn = findViewById(R.id.postulerBtn);
        
        // Only show the "Postuler" button if the user is a Jeune Diplômé
        if (jdId != -1) {
            postulerBtn.setVisibility(View.VISIBLE);
            postulerBtn.setOnClickListener(v -> {
                Pair<String, String> jdInfo = dbHelper.getJdNameAndPhone(jdId);
                int societeId = dbHelper.getSocieteIdForOffre(offre.getId());

                if (jdInfo != null && societeId != -1) {
                    boolean isSuccess = dbHelper.addCandidature(jdId, societeId, offre.getTypeMetiers(), jdInfo.first, jdInfo.second);
                    if (isSuccess) {
                        Toast.makeText(this, "Votre candidature a été envoyée!", Toast.LENGTH_SHORT).show();
                        postulerBtn.setEnabled(false); // Prevent multiple applications
                    } else {
                        Toast.makeText(this, "Erreur: Impossible d\'envoyer la candidature", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Erreur: Impossible de récupérer les informations nécessaires", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            postulerBtn.setVisibility(View.GONE);
        }
    }

    private void populateViews() {
        getSupportActionBar().setTitle(offre.getTypeMetiers());

        TextView nomSociete = findViewById(R.id.nomSociete);
        TextView formation = findViewById(R.id.formation);
        TextView salaire = findViewById(R.id.salaire);
        TextView competences = findViewById(R.id.competences);
        TextView description = findViewById(R.id.description);

        nomSociete.setText("Nom de société : " + offre.getEntreprise());
        formation.setText(offre.getFormationDemande());
        salaire.setText("Salaire: " + offre.getOffreSalaire() + " DT");
        competences.setText(offre.getCompetences());
        description.setText(offre.getDescription());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
