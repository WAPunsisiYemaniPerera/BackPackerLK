package com.example.backpackerlk.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.backpackerlk.EditEvent;
import com.example.backpackerlk.Events;
import com.example.backpackerlk.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Events> eventList;
    private FirebaseFirestore db;
    private Context context; // Add context variable

    public EventAdapter(List<Events> eventList, Context context) {
        this.eventList = eventList;
        this.db = FirebaseFirestore.getInstance(); // Initialize Firestore
        this.context = context; // Initialize context
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Events event = eventList.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventLocation.setText("Location: " + event.getLocation());
        holder.eventPrice.setText("Price: " + event.getPrice());
        holder.eventTelephone.setText("Telephone: " + event.getTelephone());
        holder.eventCategory.setText("Category: " + event.getCategory()); // Display category

        // Load image using Glide
        String imageUrl = event.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.eventImage);
        } else {
            holder.eventImage.setImageResource(R.drawable.error_image);
        }

        // Handle Edit Event Button Click
        holder.editEvent.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditEvent.class);
            intent.putExtra("eventId", event.getEventId()); // Pass the event ID
            ((Activity) v.getContext()).startActivityForResult(intent, 1); // Use startActivityForResult
        });

        // Handle Delete Event Button Click
        holder.deleteEvent.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Delete event from Firestore
                            deleteEventFromFirestore(event.getEventId(), position);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // Delete event from Firestore
    private void deleteEventFromFirestore(String eventId, int position) {
        db.collection("events").document(eventId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove the event from the list
                    eventList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, eventList.size());
                    Toast.makeText(context, "Event deleted successfully!", Toast.LENGTH_SHORT).show(); // Use context
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show(); // Use context
                });
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventLocation, eventPrice, eventTelephone, eventCategory; // Added eventCategory
        ImageView eventImage;
        Button editEvent;
        ImageButton deleteEvent;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventPrice = itemView.findViewById(R.id.eventPrice);
            eventTelephone = itemView.findViewById(R.id.eventtelephone);
            eventCategory = itemView.findViewById(R.id.eventCategory); // Initialize eventCategory
            eventImage = itemView.findViewById(R.id.eventImage);
            editEvent = itemView.findViewById(R.id.itemevent_editEvent);
            deleteEvent = itemView.findViewById(R.id.itemevent_deleteEvent);
        }
    }
}