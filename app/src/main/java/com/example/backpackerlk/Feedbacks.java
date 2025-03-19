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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Feedbacks extends AppCompatActivity {

    private ImageView backIcon;
    private RatingBar ratingBar;
    private EditText etFeedback;
    private Button btnSubmitFeedback;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

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

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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

        // Get current user
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String email = currentUser.getEmail();

            // Fetch username from Firestore
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");

                            // Create a feedback object
                            Map<String, Object> feedbackData = new HashMap<>();
                            feedbackData.put("email", email);
                            feedbackData.put("username", username);
                            feedbackData.put("rating", rating);
                            feedbackData.put("feedback", feedback);

                            // Save feedback to Firestore
                            db.collection("feedbacks").add(feedbackData)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                                        // Clear input fields
                                        ratingBar.setRating(0);
                                        etFeedback.setText("");
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to submit feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
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