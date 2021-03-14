package com.example.carbonize;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AddApartment extends Fragment {

    EditText address, zipCode, city, residents, monthlyRent, area;
    Button button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_apartment, container, false);
        residents = v.findViewById(R.id.residents);
        button = v.findViewById(R.id.button);
        area = v.findViewById(R.id.area);

        button.setOnClickListener(v1 -> calculateResults());

        return v;
    }

    public double[] calculateResults(){

        double cArea = Double.parseDouble(String.valueOf(area.getText()));
        int cResidents = Integer.parseInt(String.valueOf(residents.getText()));
        final double[] result = new double[1];

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url ="https://ilmastodieetti.ymparisto.fi/ilmastodieetti";
        String api = "/calculatorapi/v1/HousingCalculator/InfrastructureEstimate?type=family&area=" + cArea + "&residents=" + cResidents;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        result[0] = Double.parseDouble(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error while fetching the data...");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return result;

    };
}