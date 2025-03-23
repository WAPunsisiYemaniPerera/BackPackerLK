package com.example.backpackerlk;

public class Sellers {
    private String businessName;
    private String businessAddress;
    private String telephone;
    private String pricePerPerson;
    private String imageUrl;

    public Sellers(String businessName, String businessAddress, String telephone, String pricePerPerson, String imageUrl) {
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.telephone = telephone;
        this.pricePerPerson = pricePerPerson;
        this.imageUrl = imageUrl;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getPricePerPerson() {
        return pricePerPerson;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}