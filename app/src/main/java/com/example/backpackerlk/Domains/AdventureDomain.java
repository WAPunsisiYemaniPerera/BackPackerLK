package com.example.backpackerlk.Domains;

public class AdventureDomain {
    private String title;
    private String picPath;

    public AdventureDomain(String title, String picPath) {
        this.title = title;
        this.picPath = picPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
