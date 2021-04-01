package com.example.carbonize;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.annotation.Nullable;

public class Logger {


    private String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();


    private String fileName = "Userdata of " + user + ".csv";
    private static Context context;

    private static final Logger instance = new Logger(context);

    public Logger(Context con) {
        context = con;
    }

    public static Logger getInstance() {
        return instance;
}


    //Checks if the file exists yet and creates it appropriately
    public void logApartment(String address, String city, String zipCode, int residents, String tenantName, double area, double rent, double co2) {
        String path = context.getFilesDir().getAbsolutePath();
        System.out.println(path + "/" + fileName);
        File file = new File(path + "/" + fileName);
        if (file.length() == 0) {
            initializeFile();
            writeFile(address, city, zipCode, residents, tenantName, area, rent, co2);
        }
        else {
            writeFile(address, city, zipCode, residents, tenantName, area, rent, co2);
        }
    }

    //If the file does not exist yet, initializes it with header information
    public void initializeFile() {
        try {
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fileName, context.MODE_PRIVATE));
            String userHeader = "Data below is of user: " + user + "\n";
            String dataHeader = "Address;City;Zipcode;Residents;Tenant;Area;Rent;Co2Amount\n";
            ows.write(userHeader);
            ows.write(dataHeader);
            ows.close();
        } catch (IOException e) {
            // TODO Handle writing error
            System.out.println("Initializing file failed");
            e.printStackTrace();
        }
    }

    //Writes the added apartment information to the .csv file
    public void writeFile(String address, String city, String zipCode, int residents, String tenantName, double area, double rent, double co2) {
        try {
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fileName, context.MODE_APPEND));
            String newApartment = address + ";" + city + ";" + zipCode + ";" + residents + ";" + tenantName + ";" + area + ";" + rent + ";" + co2 + "\n";
            ows.append(newApartment);
            ows.close();
        } catch (IOException e) {
            //TODO Handle writing error
            System.out.println("Writing to file failed");
            e.printStackTrace();
        }
    }

}


