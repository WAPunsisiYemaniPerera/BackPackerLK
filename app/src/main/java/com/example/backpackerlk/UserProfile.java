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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.backpackerlk.Activities.Categories;
import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Activities.WhoAreYou;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private Button edituserprofile;
    private Button feedback;
    private TextView userProfileName1, userProfileEmail1, userProfileName2, userProfileEmail2, userProfileTelephone, userProfileLocation;
    private String username;
    private DatabaseReference databaseReference;

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

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://backpackerlk-4b607-default-rtdb.firebaseio.com/").getReference("Users");

        // Initialize UI elements
        edituserprofile = findViewById(R.id.usereditprofileBtn);
        feedback = findViewById(R.id.feedbackBtn);
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
        backIcon.setOnClickListener(view -> navigateToWhoAreYou());
    }

    private void fetchUserData() {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String phone = snapshot.child("mobile").getValue(String.class);
                        String location = snapshot.child("location").getValue(String.class);

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
                    }
                } else {
                    Toast.makeText(UserProfile.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToWhoAreYou() {
        Intent intent = new Intent(UserProfile.this, WhoAreYou.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfile.this, WhoAreYou.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}