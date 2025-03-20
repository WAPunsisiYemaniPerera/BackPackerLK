package com.example.backpackerlk.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.backpackerlk.Booking;
import com.example.backpackerlk.BookingsHistoryActivity;
import com.example.backpackerlk.Dashboard;
import com.example.backpackerlk.Loging;
import com.example.backpackerlk.R;
import com.example.backpackerlk.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SellerProfile extends AppCompatActivity {

    private Button editProfileButton;
    private Button goToDashboardButton;
    private ImageView backIcon;
    private TextView sellerProfileName1, sellerProfileEmail1, sellerProfileName2, sellerProfileEmail2, sellerProfileTelephone, sellerProfileLocation;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_seller_profile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        backIcon = findViewById(R.id.icback);
        editProfileButton = findViewById(R.id.sellereditprofileBtn);
        goToDashboardButton = findViewById(R.id.gotodashboardBtn);
        sellerProfileName1 = findViewById(R.id.sellerprofile_name1);
        sellerProfileEmail1 = findViewById(R.id.sellerprofile_email1);
        sellerProfileName2 = findViewById(R.id.sellerprofile_name2);
        sellerProfileEmail2 = findViewById(R.id.sellerprofile_email2);
        sellerProfileTelephone = findViewById(R.id.sellerprofile_telephone);
        sellerProfileLocation = findViewById(R.id.sellerprofile_location);

        // Fetch and display seller data
        fetchSellerData();

        // Set edit profile button click listener
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProfile.this, EditSellerProfile.class);
            startActivity(intent);
        });

        // Set go to dashboard button click listener
        goToDashboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProfile.this, Dashboard.class);
            startActivity(intent);
        });

        // Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (itemId == R.id.nav_categories) {
                startActivity(new Intent(getApplicationContext(), Categories.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(getApplicationContext(), BookingsHistoryActivity.class));
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

        // Initialize the back icon and set an OnClickListener
        backIcon.setOnClickListener(view -> navigateToUserProfile());
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
                            String phone = documentSnapshot.getString("mobile");
                            String location = documentSnapshot.getString("location");

                            // Update UI with fetched data
                            sellerProfileName1.setText(name);
                            sellerProfileEmail1.setText(email);
                            sellerProfileName2.setText(name);
                            sellerProfileEmail2.setText(email);
                            sellerProfileTelephone.setText(phone);
                            sellerProfileLocation.setText(location);
                        } else {
                            Toast.makeText(SellerProfile.this, "Seller data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SellerProfile.this, "Failed to fetch seller data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToUserProfile() {
        Intent intent = new Intent(SellerProfile.this, UserProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SellerProfile.this, UserProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}