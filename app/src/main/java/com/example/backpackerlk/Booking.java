package com.example.backpackerlk;

import android.app.DatePickerDialog;
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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.backpackerlk.R;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class Booking extends AppCompatActivity {

    private ImageView activityImage;
    private EditText activityName, arrivalDate;
    private TextView totalAmount;
    private Button decreaseQuantity, increaseQuantity, bookNowButton;
    private int quantity = 1;
    private double pricePerPerson = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        setContentView(R.layout.activity_booking);

        // Initialize views
        activityImage = findViewById(R.id.booking_activityImage);
        activityName = findViewById(R.id.booking_activityname);
        arrivalDate = findViewById(R.id.booking_arrivaldate);
        totalAmount = findViewById(R.id.booking_totalamount);
        decreaseQuantity = findViewById(R.id.decreaseQuantityButton);
        increaseQuantity = findViewById(R.id.increaseQuantityButton);
        bookNowButton = findViewById(R.id.booking_bookNowButton);

        // Get data from Intent
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        String activityNameText = getIntent().getStringExtra("ACTIVITY_NAME");
        String priceText = getIntent().getStringExtra("PRICE");

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
                updateTotalAmount();
            }
        });

        increaseQuantity.setOnClickListener(v -> {
            quantity++;
            updateTotalAmount();
        });

        // Set up book now button
        bookNowButton.setOnClickListener(v -> {
            if (arrivalDate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select arrival date", Toast.LENGTH_SHORT).show();
            } else {
                // Handle booking logic here
                Toast.makeText(this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
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
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        totalAmount.setText(format.format(total));
    }
}