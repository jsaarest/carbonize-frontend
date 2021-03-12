package com.example.carbonize;

import java.util.Date;

public class Reservation {
    private String reservartionId;
    private String customerName;
    private String apartmentId;
    private Date reservartionStartDate;
    private Date reservationEndDate;
    private Date rentPaidDate;
    private double rent;

    public Reservation(String reservartionId, String customerName, String apartmentId, Date reservartionStartDate, Date reservationEndDate, Date rentPaidDate, double rent) {
        this.reservartionId = reservartionId;
        this.customerName = customerName;
        this.apartmentId = apartmentId;
        this.reservartionStartDate = reservartionStartDate;
        this.reservationEndDate = reservationEndDate;
        this.rentPaidDate = rentPaidDate;
        this.rent = rent;
    }

    public String getReservartionId() {
        return reservartionId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public Date getReservartionStartDate() {
        return reservartionStartDate;
    }

    public Date getReservationEndDate() {
        return reservationEndDate;
    }

    public Date getRentPaidDate() {
        return rentPaidDate;
    }

    public double getRent() {
        return rent;
    }
}
