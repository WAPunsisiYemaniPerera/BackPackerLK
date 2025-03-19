package com.example.backpackerlk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Activities.WhoAreYou;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Loging extends AppCompatActivity {

    EditText login_username, login_password;
    Button loginButton, createAccountButton;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the title bar and make the activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_loging);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        login_username = findViewById(R.id.login_username); // Username input
        login_password = findViewById(R.id.login_password); // Password input
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        // Handle login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = login_username.getText().toString().trim();
                String password = login_password.getText().toString().trim();

                // Validate inputs
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Loging.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Fetch user data from Firestore using the username
                db.collection("users")
                        .whereEqualTo("username", username) // Query Firestore for the username
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Get the first document (username should be unique)
                                DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                                String email = document.getString("email"); // Fetch the email
                                String role = document.getString("role"); // Fetch the role

                                // Authenticate the user using the fetched email and password
                                auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = auth.getCurrentUser();
                                                if (user != null) {
                                                    // Redirect based on role
                                                    Intent intent;
                                                    if ("Seller".equals(role)) {
                                                        intent = new Intent(Loging.this, Dashboard.class);
                                                    } else {
                                                        intent = new Intent(Loging.this, Home.class);
                                                    }
                                                    startActivity(intent);
                                                    finish(); // Close the Loging activity
                                                }
                                            } else {
                                                Toast.makeText(Loging.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(Loging.this, "Username not found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Loging.this, "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Handle sign-up button click
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loging.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}