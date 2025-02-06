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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.backpackerlk.Activities.Categories;
import com.example.backpackerlk.Activities.DetailActivity;
import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Activities.WhoAreYou;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Air_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_air);

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
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ScrollView scrollView = findViewById(R.id.main1); // Use the ID of your ScrollView
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void navigateToCategories() {
        Intent intent = new Intent(Air_activity.this, Categories.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Apply back transition
        finish(); // Close the current activity to prevent the user from coming back to it
    }
}