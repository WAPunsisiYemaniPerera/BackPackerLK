package com.example.backpackerlk;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.backpackerlk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Booking extends AppCompatActivity {

    private ImageView activityImage;
    private EditText activityName, arrivalDate;
    private TextView totalAmount, quantityText;
    private Button decreaseQuantity, increaseQuantity, bookNowButton;
    private int quantity = 1;
    private double pricePerPerson = 0;
    private String sellerName = "";

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_booking);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        // Initialize views
        activityImage = findViewById(R.id.booking_activityImage);
        activityName = findViewById(R.id.booking_activityname);
        arrivalDate = findViewById(R.id.booking_arrivaldate);
        totalAmount = findViewById(R.id.booking_totalamount);
        quantityText = findViewById(R.id.booking_quantity);
        decreaseQuantity = findViewById(R.id.decreaseQuantityButton);
        increaseQuantity = findViewById(R.id.increaseQuantityButton);
        bookNowButton = findViewById(R.id.booking_bookNowButton);

        // Get data from Intent
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        String activityNameText = getIntent().getStringExtra("ACTIVITY_NAME");
        String priceText = getIntent().getStringExtra("PRICE");
        sellerName = getIntent().getStringExtra("SELLER_NAME");

        // Set data to views
        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(activityImage);
        }

        if (activityNameText != null) {
            activityName.setText(activityNameText);
        }

        if (priceText != null) {
            // Remove currency symbols and parse price
            String numericPrice = priceText.replaceAll("[^0-9.]", "");
            try {
                pricePerPerson = Double.parseDouble(numericPrice);
                updateTotalAmount();
            } catch (NumberFormatException e) {
                pricePerPerson = 0;
            }
        }

        // Set up date picker
        arrivalDate.setOnClickListener(v -> showDatePickerDialog());

        // Set up quantity buttons
        decreaseQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
                updateTotalAmount();
            }
        });

        increaseQuantity.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
            updateTotalAmount();
        });

        // Set up book now button
        bookNowButton.setOnClickListener(v -> {
            if (arrivalDate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select arrival date", Toast.LENGTH_SHORT).show();
            } else {
                saveBookingToFirestore();
            }
        });

        // Back button
        ImageView backButton = findViewById(R.id.icback);
        backButton.setOnClickListener(v -> finish());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    arrivalDate.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void updateTotalAmount() {
        double total = quantity * pricePerPerson;
        String formattedAmount = String.format("LKR %,.2f", total);
        totalAmount.setText(formattedAmount);
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking Confirmed!");
        builder.setMessage("Enjoy your booking!");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Redirect to BookingHistoryActivity
            startActivity(new Intent(Booking.this, com.example.backpackerlk.BookingsHistoryActivity.class));
            finish(); // Close the current activity
        });
        builder.setCancelable(false); // Prevent dismissing by tapping outside
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveBookingToFirestore() {
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current timestamp
        String bookingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        // Get username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Get seller ID and name from Intent
        String sellerId = getIntent().getStringExtra("SELLER_ID");
        String sellerName = getIntent().getStringExtra("SELLER_NAME");

        // Create booking data
        Map<String, Object> booking = new HashMap<>();
        booking.put("bookingId", "");
        booking.put("activityName", activityName.getText().toString());
        booking.put("userName", username);
        booking.put("userEmail", currentUser.getEmail());
        booking.put("userId", currentUser.getUid());
        booking.put("quantity", quantity);
        booking.put("arrivalDate", arrivalDate.getText().toString());
        booking.put("totalAmount", totalAmount.getText().toString());
        booking.put("sellerName", sellerName);
        booking.put("sellerId", sellerId);
        booking.put("bookingDate", bookingDate);
        booking.put("status", "Pending");

        // Save to Firestore
        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    db.collection("bookings").document(documentReference.getId())
                            .update("bookingId", documentReference.getId())
                            .addOnSuccessListener(aVoid -> {
                                showSuccessDialog(); // Show success dialog
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error updating booking ID", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}