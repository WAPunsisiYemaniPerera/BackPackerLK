package com.example.backpackerlk.Activities; // Replace with your actual package name

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backpackerlk.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // Replace with your actual layout name if different

        // Initialize your existing functionality (if any)
        // For example: loading data from Intent or database, setting up UI elements, etc.

        // Find the TextViews for the counters
        TextView visitorsCounter = findViewById(R.id.visitorCount);
        TextView touristsCounter = findViewById(R.id.touristCount);
        TextView localsCounter = findViewById(R.id.localCount);

        // Start animations for each counter
        animateCounter(visitorsCounter, 0, 1000);  // Visitors: 0 to 1000+
        animateCounter(touristsCounter, 0, 500);  // Tourists: 0 to 500+
        animateCounter(localsCounter, 0, 2000); // Locals: 0 to 1,000,000+
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
}
