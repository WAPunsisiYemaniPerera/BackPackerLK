package com.example.backpackerlk.Activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backpackerlk.R;

public class WhoAreYou extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_you); // Use your layout XML

        // Find the ImageView by ID
        ImageView imageView = findViewById(R.id.imageView);

        // Create the fade-in animation
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
        fadeIn.setDuration(1000); // Set the duration to 1 second (1000 milliseconds)

        // Start the animation
        fadeIn.start();
    }
}
