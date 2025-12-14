package com.example.projetandroid;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddOffreActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText typeMetiers, offreSalaire, competences, description;
    private Spinner formationDemandeSpinner;
    private int societeId; // We'll get this from the Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offre);

        dbHelper = new DatabaseHelper(this);
        societeId = getIntent().getIntExtra("SOCIETE_ID", -1);

        if (societeId == -1) {
            Toast.makeText(this, "Erreur: ID de la société non trouvé", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initializeViews();
        setupFormationSpinner();

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        Button publishBtn = findViewById(R.id.publishBtn);
        publishBtn.setOnClickListener(v -> handlePublish());
    }

    private void initializeViews() {
        typeMetiers = findViewById(R.id.typeMetiers);
        formationDemandeSpinner = findViewById(R.id.formationDemandeSpinner);
        offreSalaire = findViewById(R.id.offreSalaire);
        competences = findViewById(R.id.competences);
        description = findViewById(R.id.description);
    }

    private void setupFormationSpinner() {
        String[] diplomaTypes = {"Baccalauréat", "Licence", "Ingénieur"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diplomaTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formationDemandeSpinner.setAdapter(adapter);
    }

    private void handlePublish() {
        String typeMetiersText = typeMetiers.getText().toString();
        String formationDemandeText = formationDemandeSpinner.getSelectedItem().toString();
        String offreSalaireText = offreSalaire.getText().toString();
        String competencesText = competences.getText().toString();
        String descriptionText = description.getText().toString();

        if (typeMetiersText.isEmpty() || formationDemandeText.isEmpty() || offreSalaireText.isEmpty() || competencesText.isEmpty() || descriptionText.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        int salaire = Integer.parseInt(offreSalaireText);

        boolean isSuccess = dbHelper.addOffre(typeMetiersText, formationDemandeText, salaire, competencesText, descriptionText, societeId);

        if (isSuccess) {
            Toast.makeText(this, "Offre publiée avec succès", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erreur: Impossible de publier l'offre", Toast.LENGTH_SHORT).show();
        }
    }
}
