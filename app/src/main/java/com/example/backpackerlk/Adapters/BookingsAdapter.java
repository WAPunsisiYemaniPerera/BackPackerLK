package com.example.backpackerlk.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.BookingItem;
import com.example.backpackerlk.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private List<BookingItem> bookingsList;
    private FirebaseFirestore db;
    private Context context;

    public BookingsAdapter(List<BookingItem> bookingsList, Context context) {
        this.bookingsList = bookingsList;
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingItem booking = bookingsList.get(position);

        holder.activityName.setText(booking.getActivityName());
        holder.bookingDate.setText("Date: " + booking.getBookingDate());
        holder.totalAmount.setText("Total: " + booking.getTotalAmount());
        holder.bookingStatus.setText("Status: " + booking.getBookingStatus());

        // Cancel button - shows confirmation dialog
        holder.cancelButton.setOnClickListener(v -> {
            if (booking.getBookingStatus().equals("Pending")) {
                showCancelConfirmationDialog(booking.getBookingId(), position);
            } else {
                Toast.makeText(context, "Only pending bookings can be cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete button - only removes from UI
        holder.deleteButton.setOnClickListener(v -> {
            removeBookingFromUI(position);
        });
    }

    private void showCancelConfirmationDialog(String bookingId, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirm Cancellation")
                .setMessage("Do you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    updateBookingStatus(bookingId, "Cancelled", position);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Keep status as "Pending"
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void updateBookingStatus(String bookingId, String newStatus, int position) {
        db.collection("bookings").document(bookingId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    bookingsList.get(position).setBookingStatus(newStatus);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Booking cancelled successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to cancel booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void removeBookingFromUI(int position) {
        bookingsList.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(context, "Removed from view", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView activityName, bookingDate, totalAmount, bookingStatus;
        Button cancelButton, deleteButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.activityName);
            bookingDate = itemView.findViewById(R.id.bookingDate);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            bookingStatus = itemView.findViewById(R.id.bookingStatus);
            cancelButton = itemView.findViewById(R.id.bookingitem_cancelButton);
            deleteButton = itemView.findViewById(R.id.bookingitem_deleteButton);
        }
    }
}