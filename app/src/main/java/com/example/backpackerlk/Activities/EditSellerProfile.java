package com.example.backpackerlk.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.backpackerlk.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditSellerProfile extends AppCompatActivity {

    private EditText editName, editUsername, editEmail, editLocation, editPassword, editConfirmPassword;
    private Button saveButton;
    private ImageButton btnTogglePassword, btnToggleConfirmPassword;
    private boolean isPasswordVisible = false;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_edit_seller_profile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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

        // Initialize the back icon and set an OnClickListener
        ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToSellerProfile());
    }

    private void fetchSellerData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("sellers").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String location = documentSnapshot.getString("location");
                            String password = documentSnapshot.getString("password");
                            String username = documentSnapshot.getString("username");

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
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditSellerProfile.this, "Failed to fetch seller data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSellerData() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String location = editLocation.getText().toString();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        String username = editUsername.getText().toString();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || location.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Create a map to update the seller data
            Map<String, Object> sellerUpdates = new HashMap<>();
            sellerUpdates.put("name", name);
            sellerUpdates.put("email", email);
            sellerUpdates.put("location", location);
            sellerUpdates.put("password", password);
            sellerUpdates.put("username", username);

            // Update Firestore document for sellers
            db.collection("sellers").document(userId)
                    .update(sellerUpdates)
                    .addOnSuccessListener(aVoid -> {
                        // Update Firestore document for users
                        db.collection("users").document(userId)
                                .update(sellerUpdates)
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                    navigateToSellerProfile();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to update user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to update seller data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
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