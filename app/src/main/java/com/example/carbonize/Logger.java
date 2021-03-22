package com.example.carbonize;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Logger {
    private FirebaseAuth mAuth;
    private String user = mAuth.getCurrentUser().getEmail();
    private String fileName = "Userdata of " + user + ".csv";
    private static Context context;

    private static final Logger instance = new Logger(context);

    public Logger(Context con) {
        context = con;
    }

    public static Logger getInstance() {
        return instance;
}

    public void logApartment(String address, String city, double rent, double co2) {
        String path = context.getFilesDir().getAbsolutePath();
        System.out.println(path + "/" + fileName);
        File file = new File(path + "/" + fileName);
        if (file.length() == 0) {
            initializeFile();
            writeFile(address, city, rent, co2);
        }
        else {
            writeFile(address, city, rent, co2);
        }
    }

    public void initializeFile() {
        try {
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fileName, context.MODE_PRIVATE));
            String userHeader = "Data below is of user: " + user;
            String dataHeader = "Address, City, Rent, Co2Amount\n";
            ows.write(userHeader);
            ows.write(dataHeader);
            ows.close();
        } catch (IOException e) {
            // TODO Handle writing error
            System.out.println("Initializing file failed");
            e.printStackTrace();
        }
    }

    //public int writeFile(String apartment, String city, String address, double rent, double co2)
    public void writeFile(String address, String city, double rent, double co2) {
        try {
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fileName, context.MODE_APPEND));
            String newApartment = address + ";" + city + ";" + rent + ";" + co2 + "\n";
            ows.append(newApartment);
            ows.close();
        } catch (IOException e) {
            //TODO handle error
            System.out.println("Writing to file failed");
            e.printStackTrace();
        }
    }
}
