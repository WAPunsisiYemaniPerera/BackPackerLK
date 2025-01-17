package com.example.backpackerlk.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Adapters.AdventureAdapter;
import com.example.backpackerlk.Adapters.PopularAdapter;
import com.example.backpackerlk.Domains.AdventureDomain;
import com.example.backpackerlk.Domains.PopularDomain;
import com.example.backpackerlk.R;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private RecyclerView.Adapter adapterPopular, adapterAdventure;
    private RecyclerView recyclerViewPopular, recyclerViewAdventure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initRecyclerView();

        // Find the "seeall1" TextView
        TextView seeAllText = findViewById(R.id.seeall1);

        // Set an OnClickListener for "seeall1"
        seeAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to DetailActivity
                Intent intent = new Intent(Home.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }

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
