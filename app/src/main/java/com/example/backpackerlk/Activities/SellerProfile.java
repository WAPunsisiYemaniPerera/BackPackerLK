package com.example.backpackerlk.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.backpackerlk.Dashboard;

import com.example.backpackerlk.R;

public class SellerProfile extends AppCompatActivity {

    private Button editProfileButton;
    private Button goToDashboardButton;
    private ImageView backIcon;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar


        setContentView(R.layout.activity_seller_profile);

        // Initialize UI components
        backIcon = findViewById(R.id.icback);
        editProfileButton = findViewById(R.id.editprofileBtn);
        goToDashboardButton = findViewById(R.id.gotodashboardBtn);


        // Set edit profile button click listener
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProfile.this, EditSellerProfile.class);
            startActivity(intent);
        });

        // Set go to dashboard button click listener
        goToDashboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProfile.this, Dashboard.class);
            startActivity(intent);
        });
    }


}
