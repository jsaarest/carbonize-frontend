package com.example.carbonize;

public class Apartment {
    private String apartmentId;
    private String address;
    private String zipCode;
    private String city;
    private String owner;
    private String apartmentImageUrl;
    private Integer residents;
    private double co2Amount;
    private double area;
    private double rent;

    public Apartment(String apartmentId, String address, String zipCode, String city, String owner, String apartmentImageUrl, Integer residents, double co2Amount, double area, double rent) {
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
