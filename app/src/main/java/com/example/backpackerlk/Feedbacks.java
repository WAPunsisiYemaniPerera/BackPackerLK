package com.example.backpackerlk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Feedbacks extends AppCompatActivity {

    private ImageView backIcon;
    private RatingBar ratingBar;
    private EditText etFeedback;
    private Button btnSubmitFeedback;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedbacks);

        // Hide the action bar (with a null check)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        ratingBar = findViewById(R.id.feedback_ratingBar);
        etFeedback = findViewById(R.id.feedback_feedback);
        btnSubmitFeedback = findViewById(R.id.feedback_btn_submit_feedback);

        // Initialize the back icon
        backIcon = findViewById(R.id.icback);

        // Set click listener for the back icon
        backIcon.setOnClickListener(view -> navigateToUserProfile());

        // Handle submit button click
        btnSubmitFeedback.setOnClickListener(v -> submitFeedback());

        // Adjust padding for system insets (null check added)
        if (findViewById(R.id.main) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    private void submitFeedback() {
        int rating = (int) ratingBar.getRating();
        String feedback = etFeedback.getText().toString().trim();

        if (feedback.isEmpty()) {
            Toast.makeText(this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Send feedback to your server or save it locally
        Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();

        // Clear input fields
        ratingBar.setRating(1); // Set to minimum valid rating instead of 0
        etFeedback.setText("");
    }

    // **BACK BUTTON IN PHONE**
    @Override
    public void onBackPressed() {
        // Navigate back to the previous activity in the stack
        super.onBackPressed();
        Intent intent = new Intent(Feedbacks.this, UserProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Apply back transition
        finish();

    }

    private void navigateToUserProfile() {
        Intent intent = new Intent(Feedbacks.this, UserProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Apply back transition
        finish();
    }
}
