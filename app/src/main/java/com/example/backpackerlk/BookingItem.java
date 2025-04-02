package com.example.backpackerlk;

public class BookingItem {
    private String activityName;
    private String bookingDate;
    private String totalAmount;
    private String bookingStatus;
    private String bookingId; // Added bookingId field

    public BookingItem(String activityName, String bookingDate, String totalAmount, String bookingStatus, String bookingId) {
        this.activityName = activityName;
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
        this.bookingId = bookingId;
    }

    // Getters and Setters
    public String getActivityName() {
        return activityName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}