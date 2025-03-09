package com.example.backpackerlk;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backpackerlk.Activities.DetailActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Booking extends AppCompatActivity {

    private EditText nameEditText;
    private TextView quantityTextView;
    private EditText arrivalDateEditText;
    private TextView totalAmountTextView;
    private Button decreaseQuantityButton;
    private Button increaseQuantityButton;
    private Button bookNowButton;
    private ImageView backButton;

    private int quantity = 1;
    private double pricePerItem = 100.00; // Replace with actual price from RecyclerView item
    private String sourceActivity; // To store the source activity name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_booking);

        // Get the source activity from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SOURCE_ACTIVITY")) {
            sourceActivity = intent.getStringExtra("SOURCE_ACTIVITY");
        }

        // Initialize views
        nameEditText = findViewById(R.id.booking_activityname);
        quantityTextView = findViewById(R.id.booking_quantity);
        arrivalDateEditText = findViewById(R.id.booking_arrivaldate);
        totalAmountTextView = findViewById(R.id.booking_totalamount);
        decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);
        increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
        bookNowButton = findViewById(R.id.booking_bookNowButton);
        backButton = findViewById(R.id.icback);

        // Set initial quantity and total amount
        updateQuantityAndTotal();

        // Decrease quantity button click listener
        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    updateQuantityAndTotal();
                }
            }
        });

        // Increase quantity button click listener
        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                updateQuantityAndTotal();
            }
        });

        // Arrival date picker
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year); // Set the year
                calendar.set(Calendar.MONTH, month); // Set the month
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // Set the day of the month
                updateArrivalDateEditText(calendar);
            }
        };

        arrivalDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Booking.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Book Now button click listener
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle booking logic here
                String name = nameEditText.getText().toString();
                String arrivalDate = arrivalDateEditText.getText().toString();
                double totalAmount = quantity * pricePerItem;

                // You can now proceed with the booking process
                // For example, send data to a server or save it locally
            }
        });

        // Back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity based on the source
                navigateBackToSource();
            }
        });
    }

    private void updateQuantityAndTotal() {
        quantityTextView.setText(String.valueOf(quantity));
        double totalAmount = quantity * pricePerItem;
        totalAmountTextView.setText(String.format(Locale.getDefault(), "LKR %.2f", totalAmount));
    }

    private void updateArrivalDateEditText(Calendar calendar) {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        arrivalDateEditText.setText(sdf.format(calendar.getTime()));
    }

    private void navigateBackToSource() {
        if (sourceActivity != null) {
            switch (sourceActivity) {
                case "DetailActivity":
                    startActivity(new Intent(Booking.this, DetailActivity.class));
                    break;
                case "WaterActivities":
                    startActivity(new Intent(Booking.this, WaterActivities.class));
                    break;
                case "RopeActivity":
                    startActivity(new Intent(Booking.this, RopeActivity.class));
                    break;
                case "Air_activity":
                    startActivity(new Intent(Booking.this, Air_activity.class));
                    break;
                default:
                    finish(); // Fallback to finishing the current activity
                    break;
            }
        } else {
            finish(); // Fallback to finishing the current activity
        }
    }
}