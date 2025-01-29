package com.example.backpackerlk.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.backpackerlk.R;
import com.example.backpackerlk.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Categories extends AppCompatActivity {

    private CardView safariCard, waterCard, airCard;
    private ImageView backIcon; // Declare the back icon

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
                startActivity(new Intent(getApplicationContext(), WhoAreYou.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        // Initialize the CardViews
        safariCard = findViewById(R.id.safari);
        waterCard = findViewById(R.id.cardwater);
        airCard = findViewById(R.id.cardsky);

        // Initialize the back icon
        backIcon = findViewById(R.id.icback);

        // Set click listeners for each CardView
        safariCard.setOnClickListener(view -> navigateToDetail("Safari"));
        waterCard.setOnClickListener(view -> navigateToSellerProfile("Water Activities"));
        airCard.setOnClickListener(view -> navigateToDetail("Air Sports"));

        // Set click listener for the back icon
        backIcon.setOnClickListener(view -> navigateToHome());
    }

    private void navigateToDetail(String categoryName) {
        Intent intent = new Intent(Categories.this, DetailActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName); // Pass the category name to DetailActivity
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Add animation
    }

    private void navigateToSellerProfile(String categoryName) {
        Intent intent = new Intent(Categories.this, SellerProfile.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Add animation
    }

    private void navigateToHome() {
        Intent intent = new Intent(Categories.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Apply back transition
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToHome(); // Reuse the navigateToHome method for the physical back button
    }
}
