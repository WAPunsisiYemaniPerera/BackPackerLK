package com.example.backpackerlk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.backpackerlk.Activities.Categories;
import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Activities.SellerProfile;
import com.example.backpackerlk.Activities.WhoAreYou;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    private Button edituserprofile;
    private Button feedback;
    private Button becomeSellerBtn; // New button
    private TextView userProfileName1, userProfileEmail1, userProfileName2, userProfileEmail2, userProfileTelephone, userProfileLocation;
    private String username;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set content view before accessing UI elements
        setContentView(R.layout.activity_user_profile);

        // Hide the action bar and make the activity full screen
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        if (username == null) {
            Toast.makeText(this, "User not identified", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(UserProfile.this, Loging.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Initialize UI elements
        edituserprofile = findViewById(R.id.usereditprofileBtn);
        feedback = findViewById(R.id.feedbackBtn);
        becomeSellerBtn = findViewById(R.id.becomeSellerBtn); // Initialize the "Become a Seller" button
        userProfileName1 = findViewById(R.id.userprofile_name1);
        userProfileEmail1 = findViewById(R.id.userprofile_email1);
        userProfileName2 = findViewById(R.id.userprofile_name2);
        userProfileEmail2 = findViewById(R.id.userprofile_email2);
        userProfileTelephone = findViewById(R.id.userprofile_telephone);
        userProfileLocation = findViewById(R.id.userprofile_location);

        // Fetch and display user data
        fetchUserData();

        // Edit user profile button click event
        edituserprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, EditUser.class);
                startActivity(intent);
            }
        });

        // Feedback button click event
        feedback.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfile.this, Feedbacks.class);
            startActivity(intent);
            finish();
        });

        // "Become a Seller" button click event
        becomeSellerBtn.setOnClickListener(view -> showBecomeSellerConfirmation());

        // Navigation bar logic
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
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(getApplicationContext(), com.example.backpackerlk.BookingsHistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        // Set up ScrollView listener to hide/show bottom navigation
        ScrollView scrollView = findViewById(R.id.main1);
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY < oldScrollY) {
                bottomNavigationView.animate().alpha(0f).setDuration(200).start();
            } else if (scrollY > oldScrollY) {
                bottomNavigationView.animate().alpha(1f).setDuration(200).start();
            }
        });

        // Handle window insets to avoid gaps at the top
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the back icon and set an OnClickListener
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToHome());
    }

    private void fetchUserData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("mobile");
                            String location = documentSnapshot.getString("location");

                            // Log fetched data for debugging
                            Log.d("UserProfile", "Name: " + name);
                            Log.d("UserProfile", "Email: " + email);
                            Log.d("UserProfile", "Phone: " + phone);
                            Log.d("UserProfile", "Location: " + location);

                            // Update UI with fetched data
                            userProfileName1.setText(name);
                            userProfileEmail1.setText(email);
                            userProfileName2.setText(name);
                            userProfileEmail2.setText(email);
                            userProfileTelephone.setText(phone);
                            userProfileLocation.setText(location);
                        } else {
                            Toast.makeText(UserProfile.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(UserProfile.this, "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBecomeSellerConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Become a Seller")
                .setMessage("Do you want to move to a seller account?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Save user data to the seller collection and redirect to SellerProfile
                    saveUserAsSeller();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Stay in the UserProfile activity
                    dialog.dismiss();
                })
                .show();
    }

    private void saveUserAsSeller() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch user data from Firestore
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("mobile");
                            String location = documentSnapshot.getString("location");
                            String username = documentSnapshot.getString("username");
                            String password = documentSnapshot.getString("password");
                            String confirmPassword = documentSnapshot.getString("confirmPassword");


                            // Create a seller object
                            Map<String, Object> sellerData = new HashMap<>();
                            sellerData.put("name", name);
                            sellerData.put("email", email);
                            sellerData.put("mobile", phone);
                            sellerData.put("location", location);
                            sellerData.put("username", username);
                            sellerData.put("password", password);
                            sellerData.put("confirmPassword", confirmPassword);

                            // Save seller data to Firestore
                            db.collection("sellers").document(userId)
                                    .set(sellerData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "You are now a seller!", Toast.LENGTH_SHORT).show();
                                        // Redirect to SellerProfile
                                        Intent intent = new Intent(UserProfile.this, SellerProfile.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to save seller data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(UserProfile.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfile.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}