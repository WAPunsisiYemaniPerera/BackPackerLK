package com.example.backpackerlk.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.name.setText(seller.getName());
        holder.location.setText(seller.getLocation());
        holder.phoneNumber.setText(seller.getPhoneNumber());
        holder.image.setImageResource(seller.getImageResId());
    }

    @Override
    public int getItemCount() {
        return sellerList.size();
    }

    static class SellerViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, phoneNumber;
        ImageView image;

        public SellerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sellerName);
            location = itemView.findViewById(R.id.sellerLocation);
            phoneNumber = itemView.findViewById(R.id.sellerPhone);
            image = itemView.findViewById(R.id.sellerImage);
        }
    }
}
