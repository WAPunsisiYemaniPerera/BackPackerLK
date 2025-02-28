package com.example.backpackerlk;

public class Sellers {
    private String name;
    private String location;
    private String phoneNumber;
    private String price;
    private int imageResId; // Resource ID for seller image

    public Sellers(String name, String location, String phoneNumber, String price, int imageResId) {
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
}
