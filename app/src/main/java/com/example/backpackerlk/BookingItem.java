package com.example.backpackerlk;

public class BookingItem {
    private String activityName;
    private String bookingDate;
    private String totalAmount;
    private String bookingStatus;

    public BookingItem(String activityName, String bookingDate, String totalAmount, String bookingStatus) {
        this.activityName = activityName;
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
    }

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
}