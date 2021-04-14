package com.example.carbonize;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AddApartment extends Fragment {

    EditText address, zipCode, city, residents, monthlyRent, area, tenantName;
    Button button;
    Button backToDashboardButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        address = v.findViewById(R.id.address);
        residents = v.findViewById(R.id.residents);
        button = v.findViewById(R.id.button);
        backToDashboardButton = v.findViewById(R.id.backToDashboardButton);
        area = v.findViewById(R.id.area);
        monthlyRent = v.findViewById(R.id.monthlyRent);
        city = v.findViewById(R.id.city);
        zipCode = v.findViewById(R.id.zipCode);
        tenantName = v.findViewById(R.id.tenantNameField);

        button.setOnClickListener(v1 -> {
            try {
                addApartmentToDatabase(v);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        });
        backToDashboardButton.setOnClickListener(v1 -> Navigation.findNavController(v).navigate(R.id.action_addApartment_to_dashboardFragment));


        return v;
    }

    private float calculateResults() throws ParserConfigurationException, IOException, SAXException {

        double cArea = Double.parseDouble(String.valueOf(area.getText()));
        int cResidents = Integer.parseInt(String.valueOf(residents.getText()));
        final float[] result = new float[1];

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
                        result[0] = Float.parseFloat(response);
                        databaseHandler(result[0]);
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

    private String getTextAndTrim(EditText s){
        return s.getText().toString().trim();
    }

    private void addApartmentToDatabase(View v) throws IOException, SAXException, ParserConfigurationException {

        // Make some checking if there is data inserted
        if(TextUtils.isEmpty(getTextAndTrim(address))){
            address.setError("Please, provide an address");
            return;
        }
        if(TextUtils.isEmpty(getTextAndTrim(residents))){
            residents.setError("Please, insert the amount of residents");
            return;
        }
        if(TextUtils.isEmpty(getTextAndTrim(area))){
            area.setError("Please, insert the area");
            return;
        }
        float carbon = calculateResults();

    }
    private void databaseHandler(float carbonAmount)
    {
        System.out.println("DEBUG: carbonAmount: " + carbonAmount);

        float co2Amount = 0;
        // Create a new apartment with data
        Map<String, Object> apartment = new HashMap<>();
        apartment.put("address", address.getText().toString());
        apartment.put("apartmentImageUrl", "https://source.unsplash.com/random");
        apartment.put("area", Float.parseFloat(String.valueOf(area.getText())));
        apartment.put("city", city.getText().toString());
        apartment.put("co2Amount", carbonAmount);
        apartment.put("owner", currentUser);
        apartment.put("rent", Float.parseFloat(String.valueOf(monthlyRent.getText())));
        apartment.put("residents", Integer.parseInt(String.valueOf(residents.getText())));
        apartment.put("zipCode", zipCode.getText().toString());
        apartment.put("tenantName", tenantName.getText().toString());

        // Add a new document with a generated ID
        db.collection("apartments")
                .add(apartment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("ADDED", "New apartment added with ID: " + documentReference.getId());
                        //Add the apartment data to a .csv file
                        Logger.getInstance().logApartment(address.getText().toString(), city.getText().toString(), zipCode.getText().toString(),
                                Integer.parseInt(String.valueOf(residents.getText())), tenantName.getText().toString(),
                                Float.parseFloat(String.valueOf(area.getText())), Float.parseFloat(String.valueOf(monthlyRent.getText())), co2Amount);
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