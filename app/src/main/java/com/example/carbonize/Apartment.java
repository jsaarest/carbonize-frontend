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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    private String tenantName;
    private String apartmentImageUrl;
    private Integer residents;
    private double co2Amount;
    private double area;
    private double rent;
    private Long createdAt;

    public Apartment(String apartmentId, String address, String zipCode, String city, String owner, String apartmentImageUrl, String tenantName, Integer residents, double co2Amount, double area, double rent, long createdAt) {
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
        this.createdAt = createdAt;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setApartmentImageUrl(String apartmentImageUrl) {
        this.apartmentImageUrl = apartmentImageUrl;
    }

    public void setResidents(Integer residents) {
        this.residents = residents;
    }

    public void setCo2Amount(double co2Amount) {
        this.co2Amount = co2Amount;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public String getAddress() {
        return address;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedAt() {
        return createdAt;
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
