package com.example.backpackerlk.Activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Adapters.AdventureAdapter;
import com.example.backpackerlk.Adapters.PopularAdapter;
import com.example.backpackerlk.Domains.AdventureDomain;
import com.example.backpackerlk.Domains.PopularDomain;
import com.example.backpackerlk.R;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private TextView seeall1;
    private RecyclerView.Adapter adapterPopular, adapterAdventure;
    private RecyclerView recyclerViewPopular, recyclerViewAdventure;
    private boolean countersAnimated = false; // To prevent multiple animations

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the RecyclerViews and Button
        initRecyclerView();

        // Find and set click listener for the "See All" button
        seeall1 = findViewById(R.id.seeall1);
        seeall1.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Categories.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Detect when counters are visible and animate them
        initCounterAnimationOnScroll();
    }

    /**
     * Detect when the counters are visible and animate them
     */
    private void initCounterAnimationOnScroll() {
        NestedScrollView scrollView = findViewById(R.id.scrollview); // Ensure your layout uses NestedScrollView or ScrollView
        TextView visitorsCounter = findViewById(R.id.visitorCount);
        TextView touristsCounter = findViewById(R.id.touristCount);
        TextView localsCounter = findViewById(R.id.localCount);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            // Get the location of the counters on the screen
            int[] location = new int[2];
            visitorsCounter.getLocationOnScreen(location);
            int yPosition = location[1];

            // Get the screen height
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            // Check if the counters are visible on the screen
            if (yPosition < screenHeight && !countersAnimated) {
                countersAnimated = true; // Prevent multiple animations
                animateCounter(visitorsCounter, 0, 1000);  // Visitors: 0 to 1000+
                animateCounter(touristsCounter, 0, 500);   // Tourists: 0 to 500+
                animateCounter(localsCounter, 0, 2000);    // Locals: 0 to 2000+
            }
        });
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

    // Initialize the RecyclerViews for Popular and Adventure lists
    private void initRecyclerView() {
        // Initialize Popular List
        ArrayList<PopularDomain> popularList = new ArrayList<>();
        popularList.add(new PopularDomain(
                "Marble Beach",
                "Marble Beach",
                "Located near Trincomalee, Marble Beach is renowned for its pristine white sand and crystal-clear water that shimmers like marble under the sunlight. The beach is perfect for a peaceful day, offering opportunities for sunbathing, swimming, and taking in the scenic views of the surrounding hills. It’s managed by the Sri Lankan Air Force, ensuring cleanliness and safety for visitors.",
                2,
                true,
                4.8,
                "p1",
                true,
                1000
        ));
        popularList.add(new PopularDomain(
                "Jungle Beach",
                "Jungle Beach",
                "Nestled near Unawatuna, Jungle Beach is a hidden gem surrounded by lush greenery, offering a serene and secluded escape. Its calm, turquoise waters make it ideal for swimming, snorkeling, and enjoying marine life like colorful fish and corals. The tranquil ambiance and natural beauty make it a perfect spot for relaxation and nature lovers.",
                2,
                false,
                4.8,
                "p1",
                false,
                2800
        ));
        popularList.add(new PopularDomain(
                "Hikkaduwa",
                "Hikka",
                "Famous for its vibrant nightlife and coral reefs, Hikkaduwa is a hotspot for tourists seeking adventure and fun. Known for its surfing waves, snorkeling, and diving experiences, visitors can explore the colorful underwater world and even spot sea turtles. The beach town is lively, with beachside restaurants, shops, and a laid-back vibe that attracts travelers from all over the world.",
                2,
                true,
                4.8,
                "p1",
                true,
                1000
        ));

        // Set up RecyclerView for Popular List
        recyclerViewPopular = findViewById(R.id.view_pop);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterPopular = new PopularAdapter(popularList);
        recyclerViewPopular.setAdapter(adapterPopular);

        // Initialize Adventure List
        ArrayList<AdventureDomain> adventureList = new ArrayList<>();
        adventureList.add(new AdventureDomain("Hiking", "a1"));
        adventureList.add(new AdventureDomain("Snorkeling", "a1"));
        adventureList.add(new AdventureDomain("Camping", "a1"));
        adventureList.add(new AdventureDomain("Rock Climbing", "a1"));
        adventureList.add(new AdventureDomain("Paragliding", "a1"));

        // Set up RecyclerView for Adventure List
        recyclerViewAdventure = findViewById(R.id.view_adv);
        recyclerViewAdventure.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterAdventure = new AdventureAdapter(adventureList);
        recyclerViewAdventure.setAdapter(adapterAdventure);
    }
}
