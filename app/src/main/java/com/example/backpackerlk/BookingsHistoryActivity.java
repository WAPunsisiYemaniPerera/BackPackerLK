package com.example.backpackerlk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Activities.Categories;
import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Adapters.BookingsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingsHistoryActivity extends AppCompatActivity {

    private RecyclerView bookingsRecyclerView;
    private BookingsAdapter adapter;
    private List<BookingItem> bookingsList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_bookings_history);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize views
        ImageView backIcon = findViewById(R.id.icback);
        bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize bookings list
        bookingsList = new ArrayList<>();
        adapter = new BookingsAdapter(bookingsList, this);
        bookingsRecyclerView.setAdapter(adapter);

        // Back button click listener
        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(BookingsHistoryActivity.this, Home.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // Fetch bookings for the current user
        fetchUserBookings();
    }

    private void fetchUserBookings() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "";

        if (username.isEmpty() || userEmail.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("bookings")
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookingsList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BookingItem booking = new BookingItem(
                                    document.getString("activityName"),
                                    document.getString("arrivalDate"),
                                    document.getString("totalAmount"),
                                    document.getString("status"),
                                    document.getId() // Store the Firestore document ID
                            );
                            bookingsList.add(booking);
                        }
                        adapter.notifyDataSetChanged();

                        if (bookingsList.isEmpty()) {
                            Toast.makeText(this, "No bookings found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error fetching bookings: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BookingsHistoryActivity.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}