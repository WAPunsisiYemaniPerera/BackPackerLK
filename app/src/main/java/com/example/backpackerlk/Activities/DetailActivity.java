package com.example.backpackerlk.Activities;

import android.animation.ValueAnimator;
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

import com.example.backpackerlk.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar


        setContentView(R.layout.activity_detail);

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


        // Initialize the back icon and set an OnClickListener
        ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToCategories()); // Call navigateToCategories when clicked

        // Retrieve the TextView displaying the phone number
        TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);

        // Set the phone number dynamically (if passed via Intent or any other source)
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

    private void navigateToCategories() {
        Intent intent = new Intent(DetailActivity.this, Categories.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Apply back transition
        finish(); // Close the current activity to prevent the user from coming back to it
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // This will handle the system back button if necessary
        navigateToCategories(); // Trigger the navigation to Categories
    }


    /**
     * Animate a counter TextView from a start value to an end value.
     *
     * @param textView The TextView to animate
     * @param start    The starting value of the counter
     * @param end      The ending value of the counter
     */
    private void animateCounter(TextView textView, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(2000); // Animation duration: 2 seconds
        animator.addUpdateListener(animation -> {
            textView.setText(animation.getAnimatedValue().toString() + "+");
        });
        animator.start();
    }
}
