package com.example.backpackerlk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.backpackerlk.Activities.Categories;
import com.example.backpackerlk.Activities.DetailActivity;
import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Activities.WhoAreYou;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Air_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the name bar (Action Bar) and make the activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // Hides the action bar

        setContentView(R.layout.activity_air);

        // Set up Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_categories);

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
                startActivity(new Intent(getApplicationContext(), WhoAreYou.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        // Set up ScrollView listener to hide/show bottom navigation
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ScrollView scrollView = findViewById(R.id.main1); // Make sure the ID is correct
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY < oldScrollY) {
                // Scrolling up - hide bottom navigation
                bottomNavigationView.animate().alpha(0f).setDuration(200).start();
            } else if (scrollY > oldScrollY) {
                // Scrolling down - show bottom navigation
                bottomNavigationView.animate().alpha(1f).setDuration(200).start();
            }
        });

        // Initialize the back icon and set an OnClickListener
        ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToCategories()); // Call navigateToCategories when clicked

        // Retrieve and display the phone number dynamically
        TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            phoneNumberTextView.setText(phoneNumber);
        }

        // Initialize the Call Now button
        Button callNowButton = findViewById(R.id.callNowButton);

        // Set OnClickListener for the Call Now button
        callNowButton.setOnClickListener(view -> {
            String numberToDial = phoneNumberTextView.getText().toString();
            if (numberToDial != null && !numberToDial.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + numberToDial));
                startActivity(intent);
            } else {
                // Handle the case where no phone number is available
                callNowButton.setEnabled(false);
                callNowButton.setText("No Phone Number Available");
            }
        });
    }

    // Navigate back to Categories activity
    private void navigateToCategories() {
        Intent intent = new Intent(Air_activity.this, Categories.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Apply back transition
        finish(); // Close the current activity to prevent the user from coming back to it
    }
}
