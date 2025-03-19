package com.example.backpackerlk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity {

    EditText signup_name, signup_username, signup_email, signup_mobile, signup_password, signup_location;
    Button signupButton;
    TextView loginText;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        signup_name = findViewById(R.id.signup_name);
        signup_username = findViewById(R.id.signup_username);
        signup_email = findViewById(R.id.signup_email);
        signup_location = findViewById(R.id.signup_location);
        signup_mobile = findViewById(R.id.signup_mobile);
        signup_password = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signupButton);
        loginText = findViewById(R.id.loginText);

        // Handle sign-up button click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signup_name.getText().toString();
                String username = signup_username.getText().toString();
                String email = signup_email.getText().toString();
                String location = signup_location.getText().toString();
                String mobile = signup_mobile.getText().toString();
                String password = signup_password.getText().toString();

                // Validate inputs
                if (name.isEmpty() || username.isEmpty() || email.isEmpty() || location.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user with Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    // Save additional user details in Firestore
                                    HelperClass helperClass = new HelperClass(name, username, email, location, mobile, password);

                                    db.collection("users").document(user.getUid())
                                            .set(helperClass)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(SignUp.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUp.this, Loging.class);
                                                startActivity(intent);
                                                finish(); // Close the SignUp activity
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(SignUp.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                Toast.makeText(SignUp.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Handle "Login" text click
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Loging.class);
                startActivity(intent);
                finish(); // Close the SignUp activity
            }
        });
    }
}