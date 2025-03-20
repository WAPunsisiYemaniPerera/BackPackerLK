package com.example.backpackerlk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Activities.Categories;
import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Activities.WhoAreYou;
import com.example.backpackerlk.Adapters.SellerAdapter;
import com.example.backpackerlk.Sellers;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Air_activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SellerAdapter sellerAdapter;
    private List<Sellers> sellerList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the name bar (Action Bar) and make the activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // Hides the action bar

        setContentView(R.layout.activity_air);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Populate seller list
        sellerList = new ArrayList<>();
        sellerList.add(new Sellers("Paramotor Benthota", "Benthota, Sri Lanka", "0714999801", "$20", R.drawable.paramoter));
        sellerList.add(new Sellers("Baloons Dambulla", "Dambulla, Sri Lanka", "0710458562", "$19", R.drawable.hot_balloon));
        sellerList.add(new Sellers("Paramotor Benthota", "Benthota, Sri Lanka", "0714999801", "$17", R.drawable.paramoter));
        sellerList.add(new Sellers("Paramotor Benthota", "Benthota, Sri Lanka", "0714999801", "$21", R.drawable.paramoter));
        sellerList.add(new Sellers("Baloons Dambulla", "Dambulla, Sri Lanka", "0710458562", "$22", R.drawable.hot_balloon));
        sellerList.add(new Sellers("Paramotor Benthota", "Benthota, Sri Lanka", "0714999801", "$20", R.drawable.paramoter));

        // Set up adapter
        sellerAdapter = new SellerAdapter(sellerList);
        recyclerView.setAdapter(sellerAdapter);

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
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(getApplicationContext(), com.example.backpackerlk.BookingsHistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        // Set up NestedScrollView listener to hide/show bottom navigation
        NestedScrollView nestedScrollView = findViewById(R.id.main1); // Use the ID of your NestedScrollView
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // Scrolling down - hide bottom navigation
                    bottomNavigationView.animate().alpha(0f).setDuration(200).start();
                } else if (scrollY < oldScrollY) {
                    // Scrolling up - show bottom navigation
                    bottomNavigationView.animate().alpha(1f).setDuration(200).start();
                }
            }
        });

        // Handle back icon click
        ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToCategories());
    }

    // Navigate back to Categories activity
    private void navigateToCategories() {
        Intent intent = new Intent(Air_activity.this, Categories.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        // Navigate to Categories activity when the mobile back button is pressed
        Intent intent = new Intent(Air_activity.this, Categories.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}