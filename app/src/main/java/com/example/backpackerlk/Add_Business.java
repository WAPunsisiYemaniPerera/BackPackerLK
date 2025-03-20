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

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth auth;

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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set layout
        setContentView(R.layout.activity_add_business);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize Cloudinary
        initCloudinary();

        // Initialize views
        autoCompleteTextView = findViewById(R.id.addbusiness_category_autocomplete);
        businessImageView = findViewById(R.id.addbusiness_business_image);
        chooseImageButton = findViewById(R.id.addbusiness_choose_image);
        etYourName = findViewById(R.id.addbusiness_yourname);
        etBusinessName = findViewById(R.id.addbusiness_business_name);
        etBusinessAddress = findViewById(R.id.addbusiness_business_address);
        etTelephone = findViewById(R.id.addbusiness_telephone);
        etPricePerPerson = findViewById(R.id.addbusiness_price_per_person);
        etDescription = findViewById(R.id.addbusiness_description);
        Button submitButton = findViewById(R.id.addbusiness_btn_submit);

        // Fetch and auto-fill seller's name
        fetchSellerName();

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

        // Submit button setup
        submitButton.setOnClickListener(v -> {
            // Validate and save event
            String yourName = etYourName.getText().toString();
            String businessName = etBusinessName.getText().toString();
            String businessAddress = etBusinessAddress.getText().toString();
            String telephone = etTelephone.getText().toString();
            String pricePerPerson = etPricePerPerson.getText().toString();
            String description = etDescription.getText().toString();

            if (yourName.isEmpty() || businessName.isEmpty() || businessAddress.isEmpty() || telephone.isEmpty() || pricePerPerson.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else if (imageUri == null) {
                Toast.makeText(this, "Please select an image!", Toast.LENGTH_SHORT).show();
            } else {
                // Upload image to Cloudinary and save event
                uploadImageToCloudinary(yourName, businessName, businessAddress, telephone, pricePerPerson, description);
            }
        });
    }

    // Fetch seller's name from Firestore and auto-fill the "Your Name" field
    private void fetchSellerName() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String sellerId = currentUser.getUid();

            db.collection("sellers").document(sellerId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String sellerName = documentSnapshot.getString("name");
                            etYourName.setText(sellerName); // Auto-fill the "Your Name" field
                        } else {
                            Toast.makeText(this, "Seller data not found!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to fetch seller data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    // Initialize Cloudinary
    private void initCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dx5i5qp4a"); // Replace with your Cloudinary cloud name
        config.put("api_key", "916958589511948"); // Replace with your Cloudinary API key
        config.put("api_secret", "N9KYfyS4Ol7JIz_QLXj1zfi7K2w"); // Replace with your Cloudinary API secret
        MediaManager.init(this, config);
    }

    // Upload image to Cloudinary and save event details to Firestore
    private void uploadImageToCloudinary(String yourName, String businessName, String businessAddress, String telephone, String pricePerPerson, String description) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique public ID for the image
        String publicId = "event_image_" + UUID.randomUUID().toString();

        // Upload image to Cloudinary
        MediaManager.get().upload(imageUri)
                .option("public_id", publicId)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        // Upload started
                        Toast.makeText(Add_Business.this, "Uploading image...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Upload progress
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        // Upload successful
                        String imageUrl = (String) resultData.get("secure_url"); // Use "secure_url" instead of "url"
                        System.out.println("Image uploaded successfully. URL: " + imageUrl);
                        Toast.makeText(Add_Business.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();

                        // Save event details to Firestore
                        saveEventToFirestore(currentUser.getUid(), yourName, businessName, businessAddress, telephone, pricePerPerson, description, imageUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        // Upload failed
                        Toast.makeText(Add_Business.this, "Failed to upload image: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Upload rescheduled
                    }
                })
                .dispatch();
    }

    // Save event details to Firestore
    private void saveEventToFirestore(String sellerId, String yourName, String businessName, String businessAddress, String telephone, String pricePerPerson, String description, String imageUrl) {
        Map<String, Object> event = new HashMap<>();
        event.put("sellerId", sellerId);
        event.put("yourName", yourName);
        event.put("businessName", businessName);
        event.put("businessAddress", businessAddress);
        event.put("telephone", telephone);
        event.put("pricePerPerson", pricePerPerson);
        event.put("description", description);
        event.put("imageUrl", imageUrl);

        // Add event to Firestore
        db.collection("events")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Event saved successfully!", Toast.LENGTH_SHORT).show();
                    navigateToDashboard();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    // Navigate back to Dashboard
    private void navigateToDashboard() {
        Intent intent = new Intent(Add_Business.this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}