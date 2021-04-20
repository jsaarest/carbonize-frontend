package com.example.carbonize;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.R)
public class CarbonAndRevenueCalculator {
    //Singleton design pattern
    private static final CarbonAndRevenueCalculator instance = new CarbonAndRevenueCalculator();

    private CarbonAndRevenueCalculator() {
    }

    public static CarbonAndRevenueCalculator getInstance() {
        return instance;
    }

    public static double totalRevenue = 0;
    public static double totalCo2 = 0;
    DashboardFragment dashboard = new DashboardFragment();
    private static ArrayList<Apartment> apartments = new ArrayList<Apartment>();

    //Calculates the total revenue and Co2 amounts from the apartment list and updates the class variables with new values.
    public void calculateTotalRevenueAndCo2() {
        totalRevenue = 0;
        totalCo2 = 0;
        apartments = dashboard.getApartments();
        for (int i = 0; i < apartments.size(); i++) {
            totalRevenue = totalRevenue + apartments.get(i).getRent();
            totalCo2 = totalCo2 + apartments.get(i).getCo2Amount();
        }
    }
}
