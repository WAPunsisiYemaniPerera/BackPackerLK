package com.example.backpackerlk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.backpackerlk.Activities.Categories;
import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Activities.WhoAreYou;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfile extends AppCompatActivity {

    private Button edituserprofile;
    private Button feedback;

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

        // Initialize UI elements
        edituserprofile = findViewById(R.id.editprofileBtn);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Edit user profile button click event
        edituserprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, EditUser.class);
                startActivity(intent);
            }
        });

        // Initialize the sign-up button
        feedback = findViewById(R.id.feedbackBtn);

        feedback.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfile.this, Feedbacks.class); // Navigate to Login
            startActivity(intent);
            finish();
        });

        // Navigation bar logic
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
            }else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(getApplicationContext(), Booking.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        // Set up ScrollView listener to hide/show bottom navigation
        ScrollView scrollView = findViewById(R.id.main1); // Use the ID of your ScrollView
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY < oldScrollY) {
                // Scrolling up - make the bottom navigation invisible
                bottomNavigationView.animate().alpha(0f).setDuration(200).start();
            } else if (scrollY > oldScrollY) {
                // Scrolling down - make the bottom navigation visible
                bottomNavigationView.animate().alpha(1f).setDuration(200).start();
            }
        });

        // Handle window insets to avoid gaps at the top
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Set padding to avoid any gap at the top or bottom
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the back icon and set an OnClickListener
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToWhoAreYou()); // Call navigateToCategories when clicked
    }

    private void navigateToWhoAreYou() {
        Intent intent = new Intent(UserProfile.this, WhoAreYou.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Apply back transition
        finish(); // Close the current activity to prevent the user from coming back to it
    }

    // **BACK BUTTON IN PHONE**
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfile.this, WhoAreYou.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish(); // Ensure the current activity is removed from the stack
    }
}
