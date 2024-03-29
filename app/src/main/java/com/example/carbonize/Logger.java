package com.example.carbonize;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Logger {
    private static Context context;

    //Singleton design pattern
    private static final Logger instance = new Logger();

    private Logger() {
    }

    public static Logger getInstance() {
        return instance;
}

public void setContext(Context context) {
        this.context = context;
}


    //Checks if the file exists yet and creates it appropriately
    public void logApartment(String address, String city, String zipCode, int residents, String tenantName, double area, double rent, double co2) {
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String fileName = "Userdata of " + user + ".csv";
        String path = context.getFilesDir().getAbsolutePath();
        System.out.println(path + "/" + fileName);
        File file = new File(path + "/" + fileName);
        if (file.length() == 0) {
            initializeFile();
            writeFile(address, city, zipCode, residents, tenantName, area, rent, co2);
            System.out.println("Created a new user data file.");
        }
        else {
            writeFile(address, city, zipCode, residents, tenantName, area, rent, co2);
            System.out.println("Apartment written to existing user data file");
        }
    }

    //If the file does not exist yet, initializes it with header information
    private void initializeFile() {
        try {
            String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String fileName = "Userdata of " + user + ".csv";
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fileName, context.MODE_PRIVATE));
            String userHeader = "Data below is of user: " + user + "\n";
            String dataHeader = "Address;City;Zipcode;Residents;Tenant;Area;Rent;Co2Amount\n";
            ows.write(userHeader);
            ows.write(dataHeader);
            ows.close();
        } catch (FileNotFoundException e) {
            System.out.println("File to initialize was not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Initializing file failed");
            e.printStackTrace();
        }
    }

    //Writes the added apartment information to the .csv file
    private void writeFile(String address, String city, String zipCode, int residents, String tenantName, double area, double rent, double co2) {
        try {
            String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String fileName = "Userdata of " + user + ".csv";
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fileName, context.MODE_APPEND));
            String newApartment = address + ";" + city + ";" + zipCode + ";" + residents + ";" + tenantName + ";" + area + ";" + rent + ";" + co2 + "\n";
            ows.append(newApartment);
            ows.close();
        } catch (FileNotFoundException e) {
            System.out.println("File to write was not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Writing to file failed");
            e.printStackTrace();
        }
    }
}
