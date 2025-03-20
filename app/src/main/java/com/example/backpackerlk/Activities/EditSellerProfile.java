package com.example.backpackerlk.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.backpackerlk.Booking;
import com.example.backpackerlk.BookingsHistoryActivity;
import com.example.backpackerlk.Dashboard;
import com.example.backpackerlk.Loging;
import com.example.backpackerlk.R;
import com.example.backpackerlk.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditSellerProfile extends AppCompatActivity {

    private EditText editName, editUsername, editEmail, editLocation, editPassword, editConfirmPassword;
    private Button saveButton;
    private ImageButton btnTogglePassword, btnToggleConfirmPassword;
    private boolean isPasswordVisible = false;
    private String username;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_edit_seller_profile);

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        if (username == null) {
            Toast.makeText(this, "User not identified", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(EditSellerProfile.this, Loging.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://backpackerlk-4b607-default-rtdb.firebaseio.com/").getReference("Users");

        // Initialize UI components
        editName = findViewById(R.id.selleredit_name);
        editUsername = findViewById(R.id.selleredit_username);
        editEmail = findViewById(R.id.selleredit_email);
        editLocation = findViewById(R.id.selleredit_location);
        editPassword = findViewById(R.id.selleredit_password);
        editConfirmPassword = findViewById(R.id.sellereditconfirm_password);
        saveButton = findViewById(R.id.sellersave_button);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);
        btnToggleConfirmPassword = findViewById(R.id.sellerbtn_toggle_password2);

        // Fetch and populate seller data
        fetchSellerData();

        // Set up password visibility toggle
        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility(editPassword, btnTogglePassword));
        btnToggleConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(editConfirmPassword, btnToggleConfirmPassword));

        // Save changes button click listener
        saveButton.setOnClickListener(v -> updateSellerData());

        // Bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.nav_categories) {
                startActivity(new Intent(getApplicationContext(), Categories.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(getApplicationContext(), BookingsHistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        // Initialize the back icon and set an OnClickListener
        ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToSellerProfile());
    }

    private void fetchSellerData() {
        databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);

                    // Populate EditText fields with fetched data
                    editName.setText(name);
                    editUsername.setText(username);
                    editEmail.setText(email);
                    editLocation.setText(location);
                    editPassword.setText(password);
                    editConfirmPassword.setText(password);
                } else {
                    Toast.makeText(EditSellerProfile.this, "Seller data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditSellerProfile.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSellerData() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String location = editLocation.getText().toString();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || location.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update data in Firebase
        databaseReference.child(username).child("name").setValue(name);
        databaseReference.child(username).child("email").setValue(email);
        databaseReference.child(username).child("location").setValue(location);
        databaseReference.child(username).child("password").setValue(password);

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        navigateToSellerProfile();
    }

    private void togglePasswordVisibility(EditText editText, ImageButton btnToggle) {
        if (isPasswordVisible) {
            // Hide password
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btnToggle.setImageResource(R.drawable.ic_eye_off);
        } else {
            // Show password
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            btnToggle.setImageResource(R.drawable.ic_eye);
        }
        isPasswordVisible = !isPasswordVisible;
        editText.setSelection(editText.getText().length());
    }

    private void navigateToSellerProfile() {
        Intent intent = new Intent(EditSellerProfile.this, SellerProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToSellerProfile();
    }
}