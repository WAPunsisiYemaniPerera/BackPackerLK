package com.example.backpackerlk;

public class Events {
    private String eventId;
    private String title;
    private String location;
    private String price;
    private String telephone;
    private String imageUrl;
    private String category; // Added category field

    public Events(String eventId, String title, String location, String price, String telephone, String imageUrl, String category) {
        this.eventId = eventId;
        this.title = title;
        this.location = location;
        this.price = price;
        this.telephone = telephone;
        this.imageUrl = imageUrl;
        this.category = category; // Initialize category
    }

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category; // Getter for category
    }
}