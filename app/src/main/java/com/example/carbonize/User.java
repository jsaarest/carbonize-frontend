package com.example.carbonize;

import com.example.carbonize.Apartment;

import java.util.ArrayList;

public class User {
    private String userId;
    private String email;
    private String userImageUrl;
    private static ArrayList<Apartment> apartments = new ArrayList<Apartment>();

    public User(String userId, String email, String userImageUrl) {
        this.userId = userId;
        this.email = email;
        this.userImageUrl = userImageUrl;
    }

    //Method to fetch apartments with desired ID:s. Returns an ArrayList of apartment objects.
    public ArrayList<Apartment> getApartments(String apartmentId) {
        return apartments;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }
}