package com.example.projetandroid;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;

public class signupActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout jdFieldsContainer, societeFieldsContainer;
    private RadioGroup userTypeRadioGroup;
    private Spinner domainSpinner, jdTypeDiplomeSpinner;
    private EditText email, password, phone;
    private EditText jdUsername, jdCin;
    private EditText societeName, societeAdresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DatabaseHelper(this);

        // Initialize Views
        initializeViews();
        setupSpinners();
        setupRadioGroupListener();

        // Back button
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        // Signup button
        Button signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(v -> handleSignup());
    }

    private void initializeViews() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        domainSpinner = findViewById(R.id.domainSpinner);
        jdFieldsContainer = findViewById(R.id.jdFieldsContainer);
        societeFieldsContainer = findViewById(R.id.societeFieldsContainer);

        // Jeune Diplômé fields
        jdUsername = findViewById(R.id.jdUsername);
        jdCin = findViewById(R.id.jdCin);
        jdTypeDiplomeSpinner = findViewById(R.id.jdTypeDiplomeSpinner);

        // Société fields
        societeName = findViewById(R.id.societeName);
        societeAdresse = findViewById(R.id.societeAdresse);
    }

    private void setupSpinners() {
        // Domain Spinner
        String[] domaines = {"Informatique", "Électronique", "Mécanique", "Génie civil", "Chimie"};
        ArrayAdapter<String> domainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, domaines);
        domainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domainSpinner.setAdapter(domainAdapter);

        // Diploma Type Spinner
        String[] diplomaTypes = {"Baccalauréat", "Licence", "Ingénieur"};
        ArrayAdapter<String> diplomaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diplomaTypes);
        diplomaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jdTypeDiplomeSpinner.setAdapter(diplomaAdapter);
    }

    private void setupRadioGroupListener() {
        userTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.jeuneDiplomeRadio) {
                jdFieldsContainer.setVisibility(View.VISIBLE);
                societeFieldsContainer.setVisibility(View.GONE);
            } else if (checkedId == R.id.societeRadio) {
                jdFieldsContainer.setVisibility(View.GONE);
                societeFieldsContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private void handleSignup() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString();
        String phoneText = phone.getText().toString();
        String selectedDomain = domainSpinner.getSelectedItem().toString();

        if (!validateCommonFields(emailText, passwordText, phoneText)) {
            return;
        }

        int selectedId = userTypeRadioGroup.getCheckedRadioButtonId();
        boolean isSuccess = false;

        if (selectedId == R.id.jeuneDiplomeRadio) {
            String username = jdUsername.getText().toString();
            String cin = jdCin.getText().toString();
            String typeDiplome = jdTypeDiplomeSpinner.getSelectedItem().toString();

            if (validateJdFields(username, cin)) {
                isSuccess = dbHelper.addJd(emailText, passwordText, username, cin, phoneText, selectedDomain, typeDiplome);
            }
        } else if (selectedId == R.id.societeRadio) {
            String name = societeName.getText().toString();
            String adresse = societeAdresse.getText().toString();

            if (validateSocieteFields(name, adresse)) {
                isSuccess = dbHelper.addSociete(emailText, passwordText, name, adresse, phoneText, selectedDomain);
            }
        }

        if (isSuccess) {
            Toast.makeText(this, "Inscription réussie ✔", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erreur: l'inscription a échoué", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateCommonFields(String email, String password, String phone) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Email invalide");
            return false;
        }
        if (password.length() < 6) {
            this.password.setError("Le mot de passe doit contenir au moins 6 caractères");
            return false;
        }
        if (!Pattern.matches("^[0-9]{8}$", phone)) {
            this.phone.setError("Le numéro de téléphone doit contenir 8 chiffres");
            return false;
        }
        return true;
    }

    private boolean validateJdFields(String username, String cin) {
        if (!Pattern.matches("^[A-Za-z]+$", username)) {
            this.jdUsername.setError("Le nom d'utilisateur ne doit contenir que des lettres");
            return false;
        }
        if (!Pattern.matches("^[0-9]{8}$", cin)) {
            this.jdCin.setError("Le CIN doit contenir 8 chiffres");
            return false;
        }
        return true;
    }

    private boolean validateSocieteFields(String name, String adresse) {
        if (name.isEmpty()) {
            this.societeName.setError("Veuillez entrer le nom de la société");
            return false;
        }
        if (adresse.isEmpty()) {
            this.societeAdresse.setError("Veuillez entrer l'adresse de la société");
            return false;
        }
        return true;
    }
}
