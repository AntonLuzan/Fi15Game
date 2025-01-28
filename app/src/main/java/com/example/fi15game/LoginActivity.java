package com.example.fi15game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.loginEmailEditText);
        EditText passwordEditText = findViewById(R.id.loginPasswordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerLinkTextView = findViewById(R.id.registerLinkTextView);

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните данные", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        registerLinkTextView.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistorActivity.class);
            startActivity(intent);
        });
    }
}