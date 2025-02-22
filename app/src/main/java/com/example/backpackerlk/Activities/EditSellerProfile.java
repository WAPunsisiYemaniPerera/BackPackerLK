package com.example.backpackerlk.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.backpackerlk.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditSellerProfile extends AppCompatActivity {

    private boolean isPasswordVisible = false; // Track password visibility state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // Hide the action bar

        setContentView(R.layout.activity_edit_seller_profile);

        // Bottom navigation
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.nav_categories) {
                startActivity(new Intent(getApplicationContext(), Categories.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), WhoAreYou.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        EditText editName = findViewById(R.id.edit_name);
        EditText editUsername = findViewById(R.id.edit_username);
        EditText editEmail = findViewById(R.id.edit_email);
        EditText editPassword = findViewById(R.id.edit_password);

        // Store original hint values in tags
        editName.setTag(editName.getHint());
        editUsername.setTag(editUsername.getHint());
        editEmail.setTag(editEmail.getHint());
        editPassword.setTag(editPassword.getHint());

        // Apply TextWatcher
        setHintVisibility(editName);
        setHintVisibility(editUsername);
        setHintVisibility(editEmail);
        setHintVisibility(editPassword);

        // Handle edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the back icon and set an OnClickListener
        ImageView backIcon = findViewById(R.id.icback);
        backIcon.setOnClickListener(view -> navigateToSellerProfile());

        // Toggle Password Visibility
        ImageButton btnTogglePassword = findViewById(R.id.btn_toggle_password);
        btnTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(editPassword, btnTogglePassword);
            }
        });

        // Ensure hint is hidden if EditText is pre-filled
        if (!editPassword.getText().toString().isEmpty()) {
            editPassword.setHint("");
        }
    }

    private void navigateToSellerProfile() {
        Intent intent = new Intent(EditSellerProfile.this, SellerProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Apply back transition
        finish(); // Close the current activity to prevent the user from coming back to it
    }

    private void setHintVisibility(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    editText.setHint(editText.getTag().toString());
                } else {
                    editText.setHint("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });
    }

    private void togglePasswordVisibility(EditText editPassword, ImageButton btnTogglePassword) {
        if (isPasswordVisible) {
            // Hide password
            editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btnTogglePassword.setImageResource(R.drawable.ic_eye_off); // Set icon to "eye-off" (hidden)
        } else {
            // Show password
            editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            btnTogglePassword.setImageResource(R.drawable.ic_eye); // Set icon to "eye" (visible)
        }
        isPasswordVisible = !isPasswordVisible; // Toggle the state
        editPassword.setSelection(editPassword.getText().length()); // Move cursor to the end
    }

    // **BACK BUTTON IN PHONE**
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditSellerProfile.this, SellerProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish(); // Ensure the current activity is removed from the stack
    }
}