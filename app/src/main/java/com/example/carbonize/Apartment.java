package com.example.carbonize;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Apartment {
    private String apartmentId;
    private String address;
    private String zipCode;
    private String city;
    private String owner;
    private String apartmentImageUrl;
    private Integer residents;
    private String tenantName;
    private double co2Amount;
    private double area;
    private double rent;

    public Apartment(String apartmentId, String address, String zipCode, String city, String owner, String apartmentImageUrl, String tenantName, Integer residents, double co2Amount, double area, double rent) {
        this.apartmentId = apartmentId;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.owner = owner;
        this.apartmentImageUrl = apartmentImageUrl;
        this.residents = residents;
        this.co2Amount = co2Amount;
        this.area = area;
        this.rent = rent;
        this.tenantName = tenantName;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getOwner() {
        return owner;
    }

    public String getApartmentImageUrl() {
        return apartmentImageUrl;
    }

    public Integer getResidents() {
        return residents;
    }

    public double getCo2Amount() {
        return co2Amount;
    }

    public double getArea() {
        return area;
    }

    public double getRent() {
        return rent;
    }
}
