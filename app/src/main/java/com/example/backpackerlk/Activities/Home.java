package com.example.backpackerlk.Activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Adapters.PopularAdapter;
import com.example.backpackerlk.BookingsHistoryActivity;
import com.example.backpackerlk.Loging;
import com.example.backpackerlk.PopularItem;
import com.example.backpackerlk.R;
import com.example.backpackerlk.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private TextView seeall1;
    private RecyclerView popularRecyclerView;
    private List<PopularItem> popularItemList;
    private boolean countersAnimated = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar and make full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_home);

        // Setup navigation
        setupBottomNavigation();

        // Initialize counter animation when scrolling
        initCounterAnimationOnScroll();

        // Setup popular items
        setupPopularItems();

        // "See All" button click listener
        seeall1 = findViewById(R.id.home_seeall);
        seeall1.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);

            if (username != null) {
                Intent intent = new Intent(Home.this, Categories.class);
                intent.putExtra("username", username);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                Toast.makeText(this, "User not identified", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Loging.class));
                finish();
            }
        });

        // Social media click listeners
        setupSocialMediaButtons();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);

            if (username == null) {
                Toast.makeText(Home.this, "User not identified", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Home.this, Loging.class));
                finish();
                return false;
            }

            Intent intent;
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_categories) {
                intent = new Intent(Home.this, Categories.class);
            } else if (itemId == R.id.nav_profile) {
                intent = new Intent(Home.this, UserProfile.class);
            } else if (itemId == R.id.nav_bookings) {
                intent = new Intent(Home.this, BookingsHistoryActivity.class);
            } else {
                return false;
            }

            intent.putExtra("username", username);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            return true;
        });
    }

    private void setupSocialMediaButtons() {
        ImageView facebook = findViewById(R.id.facebookIcon);
        facebook.setOnClickListener(v -> openUrl("https://www.facebook.com/yourpage"));

        ImageView instagram = findViewById(R.id.instagramIcon);
        instagram.setOnClickListener(v -> openUrl("https://www.instagram.com/yourpage"));

        ImageView twitter = findViewById(R.id.twitterIcon);
        twitter.setOnClickListener(v -> openUrl("https://www.twitter.com/yourpage"));

        ImageView tiktok = findViewById(R.id.tiktokIcon);
        tiktok.setOnClickListener(v -> openUrl("https://www.tiktok.com/@yourpage"));
    }

    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void initCounterAnimationOnScroll() {
        NestedScrollView scrollView = findViewById(R.id.scrollview);
        TextView visitorsCounter = findViewById(R.id.visitorCount);
        TextView touristsCounter = findViewById(R.id.touristCount);
        TextView localsCounter = findViewById(R.id.localCount);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int[] location = new int[2];
            visitorsCounter.getLocationOnScreen(location);
            int yPosition = location[1];

            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            if (yPosition < screenHeight && !countersAnimated) {
                countersAnimated = true;
                animateCounter(visitorsCounter, 0, 1000);
                animateCounter(touristsCounter, 0, 500);
                animateCounter(localsCounter, 0, 2000);
            }
        });
    }

    private void animateCounter(TextView textView, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(2000);
        animator.addUpdateListener(animation ->
                textView.setText(animation.getAnimatedValue().toString() + "+")
        );
        animator.start();
    }

    private void setupPopularItems() {
        popularRecyclerView = findViewById(R.id.popularRecyclerView);
        popularRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        popularItemList = new ArrayList<>();
        popularItemList.add(new PopularItem(R.drawable.adventure, "Water Rafting", "Thrilling adventure"));
        popularItemList.add(new PopularItem(R.drawable.safari, "Safari Tour", "Explore the wildlife"));
        popularItemList.add(new PopularItem(R.drawable.kithulgala, "Day out", "Enjoy with activities"));
        popularItemList.add(new PopularItem(R.drawable.h1, "Surfing", "Step into sea"));
        popularItemList.add(new PopularItem(R.drawable.para, "Paramotoring", "Breathtaking views"));

        popularRecyclerView.setAdapter(new PopularAdapter(popularItemList));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Do you want to exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    finishAffinity();
                    System.exit(0);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}