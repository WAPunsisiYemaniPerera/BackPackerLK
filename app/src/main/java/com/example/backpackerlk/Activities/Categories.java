package com.example.backpackerlk.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.backpackerlk.R;

public class Categories extends AppCompatActivity {

    private CardView safariCard, waterCard, airCard;
    private ImageView backIcon; // Declare the back icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

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
