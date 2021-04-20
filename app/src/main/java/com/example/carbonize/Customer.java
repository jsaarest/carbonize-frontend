package com.example.carbonize;


//Basic data for customer
public class Customer {
    private String customerName;
    private Integer customerId;


    public String getTenantName() {
        return customerName;
    }

    public void setTenantName(String tenantName) {
        this.customerName = tenantName;
    }

    public Customer(String cName, Integer cId){
        customerName = cName;
        customerId = cId;
    }
}
