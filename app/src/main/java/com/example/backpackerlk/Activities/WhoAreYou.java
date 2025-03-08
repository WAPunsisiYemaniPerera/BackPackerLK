package com.example.backpackerlk.Activities;

import static com.example.backpackerlk.R.id.bottom_navigation;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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

public class WhoAreYou extends AppCompatActivity {

    private CardView travelerCard, sellerCard;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // This line hides the action bar

        setContentView(R.layout.activity_who_are_you); // Use your layout XML

        // Initialize the card views
        travelerCard = findViewById(R.id.travelerCard);
        sellerCard = findViewById(R.id.sellerCard);

        // Set click listeners for each card view
        travelerCard.setOnClickListener(view -> navigateToUser());
        sellerCard.setOnClickListener(view -> navigateToSeller());

        // Initialize the back button
        backButton = findViewById(R.id.icback);
        backButton.setOnClickListener(view -> navigateToHome());

        // Find the ImageView by ID
        ImageView imageView = findViewById(R.id.imageView);

        // Create the fade-in animation
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
        fadeIn.setDuration(1000); // Set the duration to 1 second (1000 milliseconds)

        // Start the animation
        fadeIn.start();
    }

    private void navigateToUser() {
        Intent intent = new Intent(WhoAreYou.this, UserProfile.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSeller() {
        Intent intent = new Intent(WhoAreYou.this, SellerProfile.class);
        startActivity(intent);
        finish();
    }

    private void navigateToHome() {
        Intent intent = new Intent(WhoAreYou.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    // **BACK BUTTON IN PHONE**
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(WhoAreYou.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish(); // Ensure the current activity is removed from the stack
    }
}