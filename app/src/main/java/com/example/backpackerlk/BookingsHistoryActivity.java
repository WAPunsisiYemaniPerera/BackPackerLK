package com.example.backpackerlk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Activities.Home;
import com.example.backpackerlk.Adapters.BookingsAdapter;
import java.util.ArrayList;
import java.util.List;

public class BookingsHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar

        setContentView(R.layout.activity_bookings_history);

        // Initialize the back icon
        ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the Home activity
                Intent intent = new Intent(BookingsHistoryActivity.this, Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Add transition animation
                finish(); // Close the current activity
            }
        });

        // Initialize RecyclerView
        RecyclerView bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data for bookings
        List<BookingItem> bookingsList = new ArrayList<>();
        bookingsList.add(new BookingItem("Safari Adventure", "2023-10-15", "LKR 5000.00", "Completed"));
        bookingsList.add(new BookingItem("Water Sports", "2023-10-20", "LKR 3000.00", "Pending"));
        bookingsList.add(new BookingItem("Rope Activities", "2023-10-25", "LKR 4000.00", "Completed"));

        // Set up adapter
        BookingsAdapter adapter = new BookingsAdapter(bookingsList);
        bookingsRecyclerView.setAdapter(adapter);
    }
}