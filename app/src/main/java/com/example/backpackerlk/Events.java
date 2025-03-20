package com.example.backpackerlk;

public class Events {
    private String title;
    private String location;
    private String price;
    private String telephone;
    private String imageUrl;

    public Events(String title, String location, String price, String telephone, String imageUrl) {
        this.title = title;
        this.location = location;
        this.price = price;
        this.telephone = telephone;
        this.imageUrl = imageUrl;
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
}