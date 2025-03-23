package com.example.backpackerlk.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Adapters.SellerAdapter;
import com.example.backpackerlk.BookingsHistoryActivity;
import com.example.backpackerlk.R;
import com.example.backpackerlk.Sellers;
import com.example.backpackerlk.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SellerAdapter sellerAdapter;
    private List<Sellers> sellerList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_detail);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize seller list
        sellerList = new ArrayList<>();
        sellerAdapter = new SellerAdapter(sellerList);
        recyclerView.setAdapter(sellerAdapter);

        // Fetch Safari events from Firestore
        fetchSafariEvents();

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
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(getApplicationContext(), BookingsHistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        // Set up NestedScrollView listener to hide/show bottom navigation
        NestedScrollView nestedScrollView = findViewById(R.id.main1);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                // Scrolling down - hide bottom navigation
                bottomNavigationView.animate().alpha(0f).setDuration(200).start();
            } else if (scrollY < oldScrollY) {
                // Scrolling up - show bottom navigation
                bottomNavigationView.animate().alpha(1f).setDuration(200).start();
            }
        });

        // Handle back icon click
        ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToCategories());
    }

    private void fetchSafariEvents() {
        db.collection("events")
                .whereEqualTo("category", "Safari") // Filter by category
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sellerList.clear(); // Clear existing data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Parse Firestore document into Sellers object
                            String businessName = document.getString("businessName");
                            String businessAddress = document.getString("businessAddress");
                            String telephone = document.getString("telephone");
                            String pricePerPerson = document.getString("pricePerPerson");
                            String imageUrl = document.getString("imageUrl");

                            Sellers seller = new Sellers(businessName, businessAddress, telephone, pricePerPerson, imageUrl);
                            sellerList.add(seller);
                        }
                        sellerAdapter.notifyDataSetChanged(); // Notify adapter of data change
                    } else {
                        // Handle errors
                    }
                });
    }

    private void navigateToCategories() {
        Intent intent = new Intent(DetailActivity.this, Categories.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}