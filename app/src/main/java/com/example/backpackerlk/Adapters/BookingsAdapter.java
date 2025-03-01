package com.example.backpackerlk.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.BookingItem;
import com.example.backpackerlk.R;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {
    private List<BookingItem> bookingsList;

    public BookingsAdapter(List<BookingItem> bookingsList) {
        this.bookingsList = bookingsList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingItem booking = bookingsList.get(position);
        holder.activityName.setText(booking.getActivityName());
        holder.bookingDate.setText("Date: " + booking.getBookingDate());
        holder.totalAmount.setText("Total: " + booking.getTotalAmount());
        holder.bookingStatus.setText("Status: " + booking.getBookingStatus());
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView activityName, bookingDate, totalAmount, bookingStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.activityName);
            bookingDate = itemView.findViewById(R.id.bookingDate);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            bookingStatus = itemView.findViewById(R.id.bookingStatus);
        }
    }
}