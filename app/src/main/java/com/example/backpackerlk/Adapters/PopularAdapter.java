package com.example.backpackerlk.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.PopularItem;
import com.example.backpackerlk.R;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {
    private List<PopularItem> popularItems;

    public PopularAdapter(List<PopularItem> popularItems) {
        this.popularItems = popularItems;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        PopularItem item = popularItems.get(position);
        holder.imageView.setImageResource(item.getImage());
        holder.titleText.setText(item.getTitle());
        holder.descriptionText.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return popularItems.size();
    }

    static class PopularViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText, descriptionText;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.popularImage);
            titleText = itemView.findViewById(R.id.popularTitle);
            descriptionText = itemView.findViewById(R.id.popularDesc);
        }
    }
}
