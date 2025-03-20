package com.example.backpackerlk.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.backpackerlk.Air_activity;
import com.example.backpackerlk.Booking;
import com.example.backpackerlk.BookingsHistoryActivity;
import com.example.backpackerlk.R;
import com.example.backpackerlk.RopeActivity;
import com.example.backpackerlk.UserProfile;
import com.example.backpackerlk.WaterActivities;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Categories extends AppCompatActivity {

    private CardView safariCard, waterCard, airCard, ropeCard;
    private ImageView backIcon; // Declare the back icon

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar


        setContentView(R.layout.activity_categories);

        //navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_categories);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.nav_categories){
                startActivity(new Intent(getApplicationContext(), Categories.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(getApplicationContext(), BookingsHistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        // Initialize the CardViews
        safariCard = findViewById(R.id.card_safari);
        waterCard = findViewById(R.id.card_water);
        airCard = findViewById(R.id.card_air);
        ropeCard = findViewById(R.id.rope_card);

        // Initialize the back icon
        backIcon = findViewById(R.id.icback);

        // Set click listeners for each CardView
        safariCard.setOnClickListener(view -> navigateToDetail("Safari"));
        waterCard.setOnClickListener(view -> navigateToWaterActivities("Water Activities"));
        airCard.setOnClickListener(view -> navigateToAir("Air Sports"));
        ropeCard.setOnClickListener(view -> navigateToRopeActivity("Rope Activities"));

        // Set click listener for the back icon
        backIcon.setOnClickListener(view -> navigateToHome());
    }

    private void navigateToRopeActivity(String ropeActivities) {
        Intent intent = new Intent(Categories.this, RopeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void navigateToDetail(String categoryName) {
        Intent intent = new Intent(Categories.this, DetailActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName); // Pass the category name to DetailActivity
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void navigateToWaterActivities(String categoryName) {
        Intent intent = new Intent(Categories.this, WaterActivities.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void navigateToHome() {
        Intent intent = new Intent(Categories.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void navigateToAir(String airSports) {
        Intent intent = new Intent(Categories.this, Air_activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    // **BACK BUTTON IN PHONE**
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Categories.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
