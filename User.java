package com.example.arduino;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User extends AppCompatActivity {

    TextView txtName, txtEmail;
    Button btnEditProfile, btnChangePassword, btnLogout;

    FirebaseAuth auth;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Display user info (if using Firebase)
        if (user != null) {
            txtEmail.setText(user.getEmail());
            if (user.getDisplayName() != null) {
                txtName.setText(user.getDisplayName());
            }
        }

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, Override.class);
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, LoginActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(User.this, WaypointActivity.class));
            finish();
        });
    }
}

