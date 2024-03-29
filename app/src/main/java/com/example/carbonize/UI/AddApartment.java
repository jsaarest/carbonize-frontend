package com.example.carbonize.UI;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carbonize.CustomerList;
import com.example.carbonize.Logger;
import com.example.carbonize.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class AddApartment extends Fragment {

    EditText addressEditText, zipCodeEditText, cityEditText, residentsEditText, monthlyRentEditText, areaEditText, tenantNameEditText;
    Spinner tenantSpinner;
    String address, city, tenantName, zipCode;
    Double area, monthlyRent;
    Integer residents;

    Button button;
    Button backToDashboardButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ArrayList<String> tenantsToShow = new ArrayList<>();

    public AddApartment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_apartment, container, false);

        // Find views for text fields
        button = v.findViewById(R.id.button);
        backToDashboardButton = v.findViewById(R.id.backToDashboardButton);
        addressEditText = v.findViewById(R.id.address);
        residentsEditText = v.findViewById(R.id.residents);
        areaEditText = v.findViewById(R.id.area);
        monthlyRentEditText = v.findViewById(R.id.monthlyRent);
        cityEditText = v.findViewById(R.id.city);
        zipCodeEditText = v.findViewById(R.id.zipCode);
        tenantNameEditText = v.findViewById(R.id.tenantNameField);
        tenantSpinner = v.findViewById(R.id.tenantSpinner);


        button.setOnClickListener(v1 -> {
            try {
                checkInputIntegrity(v);
            } catch (IOException | ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        });
        backToDashboardButton.setOnClickListener(v1 -> Navigation.findNavController(v).navigate(R.id.action_addApartment_to_dashboardFragment));

        //add contents of customerList to spinner
        ArrayAdapter<String> tenantAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, tenantsToShow);


       CustomerList customersToAdd = new CustomerList();
       for (int i=0; i<customersToAdd.customerList.size();i++) {
           tenantsToShow.add(customersToAdd.customerList.get(i).getTenantName());

       }
        tenantSpinner.setAdapter(tenantAdapter);

        return v;
    }


    private float callCo2Api() throws ParserConfigurationException, IOException, SAXException {


        // Calls API and returns the CO2 amount of the apartment based on inputs
        double cArea = Double.parseDouble(String.valueOf(areaEditText.getText()));
        int cResidents = Integer.parseInt(String.valueOf(residentsEditText.getText()));
        final float[] result = new float[1];

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url ="https://ilmastodieetti.ymparisto.fi/ilmastodieetti";
        String api = "/calculatorapi/v1/HousingCalculator/InfrastructureEstimate?type=family&area=" + cArea + "&residents=" + cResidents;


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + api,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.R)
                    @Override
                    public void onResponse(String response) {
                        //First (and only) result is the desired co2e amount
                        result[0] = Float.parseFloat(response);
                        addToDatabase(result[0]);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error while fetching the data...");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return result[0];
    };

    private String getTextAndTrim(EditText text){
        return text.getText().toString().trim();
    }

    private void checkInputIntegrity(View view) throws IOException, SAXException, ParserConfigurationException {

        // Make some checking if there is data inserted
        if(TextUtils.isEmpty(getTextAndTrim(addressEditText))){
            addressEditText.setError("Please, provide an address");
            return;
        }
        if(TextUtils.isEmpty(getTextAndTrim(residentsEditText))){
            residentsEditText.setError("Please, insert the amount of residents");
            return;
        }
        if(TextUtils.isEmpty(getTextAndTrim(areaEditText))){
            areaEditText.setError("Please, insert the area");
            return;
        }

        //Calling the method that gets the Co2 amount
        float carbon = callCo2Api();
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void addToDatabase(float carbonAmount)
    {

        address = addressEditText.getText().toString();
        residents = Integer.parseInt(String.valueOf(residentsEditText.getText()));
        area = Double.parseDouble(String.valueOf(areaEditText.getText()));
        monthlyRent = Double.parseDouble(String.valueOf(monthlyRentEditText.getText()));
        city = cityEditText.getText().toString();
        zipCode = zipCodeEditText.getText().toString();
        tenantName = tenantSpinner.getSelectedItem().toString();

        // Create a timestamp, so we can sort the list in dashboard later if needed
        Date date = new Date();
        Long timestamp = date.getTime();
        double formattedCarbonAmount = DashboardFragment.doubleRound(carbonAmount, 1);

        // Create a new apartment with data
        Map<String, Object> apartment = new HashMap<>();
        apartment.put("address", address);
        apartment.put("apartmentImageUrl", "https://source.unsplash.com/random");
        apartment.put("area", area);
        apartment.put("city", city);
        apartment.put("co2Amount", formattedCarbonAmount);
        apartment.put("owner", currentUser);
        apartment.put("rent", monthlyRent);
        apartment.put("residents", residents);
        apartment.put("zipCode", zipCode);
        apartment.put("tenantName", tenantName);
        apartment.put("createdAt", timestamp);

        // Add a new document with a generated ID
        db.collection("apartments")
                .add(apartment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("ADDED", "New apartment added with ID: " + documentReference.getId());
                        //Add the apartment data to a .csv file
                        Logger.getInstance().logApartment(address, city, zipCode, residents, tenantName, area, monthlyRent, formattedCarbonAmount);
                        Navigation.findNavController(getView()).navigate(R.id.action_addApartment_to_dashboardFragment);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERR", "Error adding document", e);
                    }
                });
    }

}