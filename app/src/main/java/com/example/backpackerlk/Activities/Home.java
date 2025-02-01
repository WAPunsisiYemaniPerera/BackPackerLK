package com.example.backpackerlk.Activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Adapters.PopularAdapter;
import com.example.backpackerlk.Domains.PopularDomain;
import com.example.backpackerlk.PopularItem;
import com.example.backpackerlk.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private TextView seeall1;
    private RecyclerView popularRecyclerView;
    private List<PopularItem> popularItemList;
    private boolean countersAnimated = false; // Prevent multiple animations

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

        // Find and set click listener for the "See All" button
        seeall1 = findViewById(R.id.seeall1);
        seeall1.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Categories.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
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
        popularItemList.add(new PopularItem(R.drawable.h1, "Water Rafting", "Thrilling adventure"));
        popularItemList.add(new PopularItem(R.drawable.h2, "Safari Tour", "Explore the wildlife"));
        popularItemList.add(new PopularItem(R.drawable.h1, "Camping Night", "Stay under the stars"));
        popularItemList.add(new PopularItem(R.drawable.h1, "Beach Party", "Live music & dance"));
        popularItemList.add(new PopularItem(R.drawable.h2, "Mountain Hiking", "Breathtaking views"));

        popularRecyclerView.setAdapter(new PopularAdapter(popularItemList));
    }
}
