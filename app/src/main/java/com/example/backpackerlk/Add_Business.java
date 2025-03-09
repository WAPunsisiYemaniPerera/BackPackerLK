package com.example.backpackerlk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Add_Business extends AppCompatActivity {

    // Dropdown items
    String[] item = {"Safari", "Water Activities", "Air Activities", "Rope Activities"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    // Image selection
    private static final int REQUEST_PERMISSION = 100;
    private ImageView businessImageView;
    private Button chooseImageButton;
    private Uri imageUri;

    // Form fields
    private TextInputEditText etYourName, etBusinessName, etBusinessAddress, etTelephone, etPricePerPerson, etDescription;

    // Activity Result Launcher for picking image
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    businessImageView.setImageURI(imageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen setup
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set layout
        setContentView(R.layout.activity_add_business);

        // Initialize views
        TextInputLayout textInputLayout = findViewById(R.id.addbusiness_category);
        autoCompleteTextView = findViewById(R.id.addbusiness_category_autocomplete);
        businessImageView = findViewById(R.id.addbusiness_business_image);
        chooseImageButton = findViewById(R.id.addbusiness_choose_image);
        etYourName = findViewById(R.id.addbusiness_yourname);
        etBusinessName = findViewById(R.id.addbusiness_business_name);
        etBusinessAddress = findViewById(R.id.addbusiness_business_address);
        etTelephone = findViewById(R.id.addbusiness_telephone);
        etPricePerPerson = findViewById(R.id.addbusiness_price_per_person); // New field
        etDescription = findViewById(R.id.addbusiness_description);
        Button submitButton = findViewById(R.id.addbusiness_btn_submit);

        // Dropdown setup
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener((AdapterView<?> adapterView, View view, int position, long l) -> {
            String selectedItem = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(Add_Business.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
        });

        // Back button setup
        findViewById(R.id.icback).setOnClickListener(view -> navigateToDashboard());

        // Image picker setup
        chooseImageButton.setOnClickListener(v -> checkPermissionAndPickImage());

        // Handle insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Submit button setup
        submitButton.setOnClickListener(v -> {
            // Save or update the event
            String yourName = etYourName.getText().toString();
            String businessName = etBusinessName.getText().toString();
            String businessAddress = etBusinessAddress.getText().toString();
            String telephone = etTelephone.getText().toString();
            String pricePerPerson = etPricePerPerson.getText().toString(); // New field
            String description = etDescription.getText().toString();

            if (yourName.isEmpty() || businessName.isEmpty() || businessAddress.isEmpty() || telephone.isEmpty() || pricePerPerson.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                // Save or update logic here
                Toast.makeText(this, "Event saved!", Toast.LENGTH_SHORT).show();
                navigateToDashboard();
            }
        });
    }

    // Check permission and pick image
    private void checkPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
            }
        } else { // Below Android 13
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }
        }
    }

    // Open gallery to pick an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // Navigate back to Dashboard
    private void navigateToDashboard() {
        Intent intent = new Intent(Add_Business.this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}