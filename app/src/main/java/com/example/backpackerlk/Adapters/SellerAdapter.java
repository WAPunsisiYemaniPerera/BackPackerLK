package com.example.backpackerlk.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.backpackerlk.Booking;
import com.example.backpackerlk.R;
import com.example.backpackerlk.Sellers;

import java.util.List;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {
    private List<Sellers> sellerList;

    public SellerAdapter(List<Sellers> sellerList) {
        this.sellerList = sellerList;
    }

    @NonNull
    @Override
    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_card, parent, false);
        return new SellerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerViewHolder holder, int position) {
        Sellers seller = sellerList.get(position);
        holder.name.setText(seller.getBusinessName());
        holder.location.setText(seller.getBusinessAddress());
        holder.phoneNumber.setText(seller.getTelephone());
        holder.price.setText(seller.getPricePerPerson());

        // Load image using Glide with error handling
        Glide.with(holder.itemView.getContext())
                .load(seller.getImageUrl())
                .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                .error(R.drawable.error_image) // Error image if loading fails
                .into(holder.image);

        // Set click listener for the "Book Now" button
        holder.bookNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Booking.class);
            intent.putExtra("SELLER_NAME", seller.getBusinessName());
            intent.putExtra("SELLER_PRICE", seller.getPricePerPerson());
            v.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return sellerList.size();
    }

    static class SellerViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, phoneNumber, price;
        ImageView image;
        Button bookNowButton;

        public SellerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sellercard_eventName);
            location = itemView.findViewById(R.id.sellercard_sellerLocation);
            phoneNumber = itemView.findViewById(R.id.sellercard_sellerPhone);
            price = itemView.findViewById(R.id.sellercard_sellerPrice);
            image = itemView.findViewById(R.id.sellerImage);
            bookNowButton = itemView.findViewById(R.id.sellercard_bookNowButton);
        }
    }
}