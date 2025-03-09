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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText signup_name, signup_username, signup_email, signup_mobile, signup_password, signup_location;
    Button signupButton;
    TextView loginText;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // Set the content view
        setContentView(R.layout.activity_sign_up);

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
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Users");

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

                // Create a HelperClass object
                HelperClass helperClass = new HelperClass(name, username, email, location, mobile, password);

                // Save user data to Firebase
                reference.child(username).setValue(helperClass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUp.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this, Loging.class);
                        startActivity(intent);
                        finish(); // Close the SignUp activity
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
                // Navigate to the Login activity
                Intent intent = new Intent(SignUp.this, Loging.class);
                startActivity(intent);
                finish(); // Close the SignUp activity
            }
        });
    }
}