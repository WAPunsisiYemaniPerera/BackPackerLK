package com.example.backpackerlk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputLayout;

public class Add_Business extends AppCompatActivity {
    // Array of items for the dropdown
    String[] item = {"Safari", "Water Activities", "Air Activities", "Rope Activities"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the title bar and set fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // Hide the action bar
        }

        // Set the layout before finding views
        setContentView(R.layout.activity_add_business);

        // Initialize the AutoCompleteTextView inside TextInputLayout
        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout);
        autoCompleteTextView = findViewById(R.id.auto_complete_textview);

        // Check if views are properly initialized
        if (textInputLayout == null || autoCompleteTextView == null) {
            Toast.makeText(this, "Error: Missing views in layout", Toast.LENGTH_LONG).show();
            return;
        }

        // Set up the adapter for the dropdown
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);

        // Set the listener for item selection in the dropdown
        autoCompleteTextView.setOnItemClickListener((AdapterView<?> adapterView, View view, int position, long l) -> {
            String selectedItem = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(Add_Business.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
        });

        // Back button functionality
        findViewById(R.id.icback).setOnClickListener(view -> navigateToDashboard());

        // Handle window insets for full screen compatibility
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView), (v, insets) -> {
            // Only handle system bar insets
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Handle physical back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(Add_Business.this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}