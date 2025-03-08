package com.example.backpackerlk.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Booking;
import com.example.backpackerlk.BookingItem;
import com.example.backpackerlk.R;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder> {

    private List<BookingItem> bookingsList;
    private Context context;

    public BookingsAdapter(List<BookingItem> bookingsList) {
        this.bookingsList = bookingsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingItem booking = bookingsList.get(position);
        holder.activityName.setText(booking.getActivityName());
        holder.bookingDate.setText("Date: " + booking.getBookingDate());
        holder.totalAmount.setText("Total: " + booking.getTotalAmount());
        holder.bookingStatus.setText("Status: " + booking.getBookingStatus());

        // Handle Update Button Click
        holder.updateButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, Booking.class);
            intent.putExtra("SOURCE_ACTIVITY", "BookingsHistoryActivity"); // Pass source activity
            context.startActivity(intent);
        });

        // Handle Delete Button Click
        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(position);
        });
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView activityName, bookingDate, totalAmount, bookingStatus;
        Button updateButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.activityName);
            bookingDate = itemView.findViewById(R.id.bookingDate);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            bookingStatus = itemView.findViewById(R.id.bookingStatus);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Booking");
        builder.setMessage("Are you sure you want to delete this booking?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove the item from the list
                bookingsList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, bookingsList.size());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}