package com.example.carbonize;

import java.util.ArrayList;

/*
CustomerList -class. This class also simulates backend -system of Carbonize that
holds the actual customer list. User needs to add background checked tenant candidate
for the new apartment.

 */

public class CustomerList {
    private static CustomerList customers = new CustomerList();

    public ArrayList<Customer> customerList = new ArrayList<Customer>();
    public static CustomerList getInstance(){
        return customers;
    }

    public static CustomerList getCustomers() {
        return customers;
    }
    public CustomerList(){
        //These customers are from the back-end system
        customerList.add(new Customer("Aarne Apilainen", 1));
        customerList.add(new Customer("Börje Bjönström", 2));
        customerList.add(new Customer("Camilla Carlqvist", 3));
        customerList.add(new Customer("Dahlia Dufva", 4));
        customerList.add(new Customer("Esko Eerikäinen", 5));
        customerList.add(new Customer("Filip Fariselainen", 6));
        customerList.add(new Customer("Teuvo Teurastaja", 7));
        customerList.add(new Customer("Seppo Sepittäjä", 8));
        customerList.add(new Customer("Jaakko Jaakkola", 9));
        customerList.add(new Customer("Matti Meikäläinen", 10));



    }
}
