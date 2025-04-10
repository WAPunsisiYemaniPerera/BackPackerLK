package com.example.backpackerlk;

public class HelperClass {

    private String name;
    private String username;
    private String email;
    private String location;
    private String mobile;
    private String password;
    private String role;

    // Default constructor (required for Firebase)
    public HelperClass() {
    }

    // Parameterized constructor
    public HelperClass(String name, String username, String email, String location, String mobile, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.location = location;
        this.mobile = mobile;
        this.password = password;
        this.role = "None"; // Default role
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}