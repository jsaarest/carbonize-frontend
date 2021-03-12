package com.example.carbonize;

import java.util.ArrayList;

public class Customer {
    private String customerId;
    private String customerName;
    private String email;
    private Integer totalReservations;

    public Customer(String customerId, String customerName, String email, Integer totalReservations) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.totalReservations = totalReservations;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getTotalReservations() {
        return totalReservations;
    }
}
