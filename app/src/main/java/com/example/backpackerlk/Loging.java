package com.example.backpackerlk;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Loging extends AppCompatActivity {

    EditText login_username, login_password;
    Button loginButton, createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the title bar and make the activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_loging);

        // Initialize views
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        // Handle login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() || !validatePassword()) {
                    return;
                } else {
                    checkUser();
                }
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

    public Boolean validateUsername() {
        String val = login_username.getText().toString();
        if (val.isEmpty()) {
            login_username.setError("Username cannot be empty");
            return false;
        } else {
            login_username.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = login_password.getText().toString();
        if (val.isEmpty()) {
            login_password.setError("Password cannot be empty");
            return false;
        } else {
            login_password.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = login_username.getText().toString().trim();
        String userPassword = login_password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    login_username.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                        login_username.setError(null);

                        // Save username in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", userUsername); // Save the username
                        editor.apply();

                        // Navigate to Home
                        Intent intent = new Intent(Loging.this, Home.class);
                        startActivity(intent);
                        finish(); // Close the Loging activity
                    } else {
                        login_password.setError("Invalid Credentials");
                        login_password.requestFocus();
                    }
                } else {
                    login_username.setError("User does not exist");
                    login_username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Loging.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}