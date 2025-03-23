package com.example.backpackerlk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.backpackerlk.Dashboard;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditEvent extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String eventId;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // Dropdown items
    String[] categories = {"Safari", "Water Activities", "Air Activities", "Rope Activities"};
    AutoCompleteTextView categoryDropdown;
    ArrayAdapter<String> adapterItems;
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the name bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_edit_event);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize Cloudinary
        initCloudinary();

        // Get event ID from intent
        eventId = getIntent().getStringExtra("eventId");
        if (eventId == null) {
            Toast.makeText(this, "Event ID is missing!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if eventId is not provided
            return;
        }

        // Initialize views
        TextInputEditText businessName = findViewById(R.id.editevent_business_name);
        categoryDropdown = findViewById(R.id.editevent_category_autocomplete);
        TextInputEditText address = findViewById(R.id.editevent_address2);
        TextInputEditText telephone = findViewById(R.id.editevent_telephone1);
        TextInputEditText price = findViewById(R.id.editevent_price1);
        TextInputEditText description = findViewById(R.id.editevent_description);
        ImageView eventImage = findViewById(R.id.editevent_image);
        Button chooseImageButton = findViewById(R.id.editevent_choose_image);
        Button updateButton = findViewById(R.id.editevent_btn_update);
        backIcon = findViewById(R.id.icback); // Initialize the back icon

        // Set up the dropdown for categories
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, categories);
        categoryDropdown.setAdapter(adapterItems);

        // Fetch event details from Firestore
        db.collection("events").document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Set values for all fields
                        businessName.setText(documentSnapshot.getString("businessName"));
                        address.setText(documentSnapshot.getString("businessAddress"));
                        telephone.setText(documentSnapshot.getString("telephone"));
                        price.setText(documentSnapshot.getString("pricePerPerson"));
                        description.setText(documentSnapshot.getString("description"));

                        // Pre-select the category in the dropdown
                        String category = documentSnapshot.getString("category");
                        if (category != null) {
                            categoryDropdown.setText(category, false); // Pre-select the category
                        }

                        // Load image using Glide
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this).load(imageUrl).into(eventImage);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch event details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Handle image selection
        chooseImageButton.setOnClickListener(v -> openImagePicker());
        eventImage.setOnClickListener(v -> openImagePicker());

        // Handle update button click
        updateButton.setOnClickListener(v -> {
            // Get updated values from the form
            String updatedBusinessName = businessName.getText().toString();
            String updatedCategory = categoryDropdown.getText().toString();
            String updatedAddress = address.getText().toString();
            String updatedTelephone = telephone.getText().toString();
            String updatedPrice = price.getText().toString();
            String updatedDescription = description.getText().toString();

            // Validate inputs
            if (updatedBusinessName.isEmpty() || updatedCategory.isEmpty() || updatedAddress.isEmpty() ||
                    updatedTelephone.isEmpty() || updatedPrice.isEmpty() || updatedDescription.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // If a new image is selected, upload it to Cloudinary
            if (imageUri != null) {
                uploadImageToCloudinary(updatedBusinessName, updatedCategory, updatedAddress, updatedTelephone, updatedPrice, updatedDescription);
            } else {
                // If no new image is selected, update the event without changing the image
                updateEventInFirestore(updatedBusinessName, updatedCategory, updatedAddress, updatedTelephone, updatedPrice, updatedDescription, null);
            }
        });

        // Set click listener for the back icon
        backIcon.setOnClickListener(view -> navigateToDashboard());
    }

    // Initialize Cloudinary
    private void initCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dx5i5qp4a"); // Cloudinary cloud name
        config.put("api_key", "916958589511948"); // Cloudinary API key
        config.put("api_secret", "N9KYfyS4Ol7JIz_QLXj1zfi7K2w"); // Cloudinary API secret
        MediaManager.init(this, config);
    }

    // Open image picker
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView eventImage = findViewById(R.id.editevent_image);
            eventImage.setImageURI(imageUri);
        }
    }

    // Upload image to Cloudinary
    private void uploadImageToCloudinary(String businessName, String category, String address, String telephone, String price, String description) {
        // Generate a unique public ID for the image
        String publicId = "event_image_" + UUID.randomUUID().toString();

        // Upload image to Cloudinary
        MediaManager.get().upload(imageUri)
                .option("public_id", publicId)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        // Upload started
                        Toast.makeText(EditEvent.this, "Uploading image...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Upload progress
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        // Upload successful
                        String imageUrl = (String) resultData.get("secure_url");
                        Toast.makeText(EditEvent.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();

                        // Update event in Firestore with the new image URL
                        updateEventInFirestore(businessName, category, address, telephone, price, description, imageUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        // Upload failed
                        Toast.makeText(EditEvent.this, "Failed to upload image: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Upload rescheduled
                    }
                })
                .dispatch();
    }

    // Update event in Firestore
    private void updateEventInFirestore(String businessName, String category, String address, String telephone, String price, String description, String imageUrl) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("businessName", businessName);
        updates.put("category", category);
        updates.put("businessAddress", address);
        updates.put("telephone", telephone);
        updates.put("pricePerPerson", price);
        updates.put("description", description);

        // If a new image URL is provided, update it
        if (imageUrl != null) {
            updates.put("imageUrl", imageUrl);
        }

        // Update event in Firestore
        db.collection("events").document(eventId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event updated successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Notify Dashboard to refresh
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(EditEvent.this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToDashboard();
    }
}