package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        TextView signup = findViewById(R.id.signupText);
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, signupActivity.class);
            startActivity(intent);
        });

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            Pair<DatabaseHelper.UserType, Integer> user = dbHelper.checkUser(emailText, passwordText);

            if (user != null) {
                if (user.first == DatabaseHelper.UserType.JD) {
                    Toast.makeText(this, "Login en tant que Jeune Diplômé", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, offres.class);
                    intent.putExtra("JD_ID", user.second); // Pass the user's ID
                    startActivity(intent);
                } else if (user.first == DatabaseHelper.UserType.SOCIETE) {
                    Toast.makeText(this, "Login en tant que Société", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SocieteMenuActivity.class);
                    intent.putExtra("SOCIETE_ID", user.second);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
