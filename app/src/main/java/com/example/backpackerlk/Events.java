package com.example.backpackerlk;

public class Events {
    private String title;
    private String location;
    private String price;
    private String telephone;
    private int imageResId;

    public Events(String title, String location, String price, String telephone, int imageResId) {
        this.title = title;
        this.location = location;
        this.price = price;
        this.telephone = telephone;
        this.imageResId = imageResId;
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

    public int getImageResId() {
        return imageResId;
    }
}