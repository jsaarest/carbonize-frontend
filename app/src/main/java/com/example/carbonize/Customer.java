package com.example.carbonize;


//Basic data for customer including getters & setters
public class Customer {
    private String customerName;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

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
