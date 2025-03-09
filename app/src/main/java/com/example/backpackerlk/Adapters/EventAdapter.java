package com.example.backpackerlk.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backpackerlk.Events;
import com.example.backpackerlk.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Events> eventList;

    public EventAdapter(List<Events> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Events event = eventList.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventLocation.setText(event.getLocation());
        holder.eventPrice.setText(event.getPrice());
        holder.eventTelephone.setText(event.getTelephone());
        holder.eventImage.setImageResource(event.getImageResId());

        holder.editEvent.setOnClickListener(v -> {
            // Handle edit event button click
        });

        holder.deleteEvent.setOnClickListener(v -> {
            // Handle delete event button click
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventLocation, eventPrice, eventTelephone;
        ImageView eventImage;
        Button editEvent;
        ImageButton deleteEvent;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventPrice = itemView.findViewById(R.id.eventPrice);
            eventTelephone = itemView.findViewById(R.id.eventtelephone);
            eventImage = itemView.findViewById(R.id.eventImage);
            editEvent = itemView.findViewById(R.id.itemevent_editEvent);
            deleteEvent = itemView.findViewById(R.id.itemevent_deleteEvent);
        }
    }
}