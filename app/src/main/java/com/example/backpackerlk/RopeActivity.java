package com.example.backpackerlk;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class RopeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SellerAdapter sellerAdapter;
    private List<Sellers> sellerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_rope);

        // Initialize the back icon and set an OnClickListener
        ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToCategories());

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Populate seller list
        sellerList = new ArrayList<>();
        sellerList.add(new Sellers("Flying Rawana", "Ella, Sri Lanka", "0771246987", R.drawable.rawana));
        sellerList.add(new Sellers("Sun Diving - Unawatuna", "Unawatuna, Sri Lanka", "0758657380", R.drawable.wateractivite3));
        sellerList.add(new Sellers("Amaya Beach", "Pasikudah, Sri Lanka", "0714999801", R.drawable.wateractivitye4));
        sellerList.add(new Sellers("River Adventure", "Aluthgama Bentota, Sri Lanka", "0775769769", R.drawable.wateractivite1));
        sellerList.add(new Sellers("Sun Diving - Unawatuna", "Unawatuna, Sri Lanka", "0758657380", R.drawable.wateractivite3));
        sellerList.add(new Sellers("Amaya Beach", "Pasikudah, Sri Lanka", "0714999801", R.drawable.wateractivitye4));

        // Set up adapter
        sellerAdapter = new SellerAdapter(sellerList);
        recyclerView.setAdapter(sellerAdapter);

        // Bottom navigation
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
    }

    private void navigateToCategories() {
        Intent intent = new Intent(RopeActivity.this, Categories.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToCategories();
    }
}