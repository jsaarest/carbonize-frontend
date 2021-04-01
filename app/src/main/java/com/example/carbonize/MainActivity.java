package com.example.carbonize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Passing application context onto logger for it to be able to open files
        Context context = getApplicationContext();
        Logger logger = new Logger(context);
    }
}