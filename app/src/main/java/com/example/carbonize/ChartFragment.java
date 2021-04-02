package com.example.carbonize;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ChartFragment extends Fragment {
    private BarChart chart;
    private Button backToProfile;
    private TextView revenueText;
    private TextView co2Text;
    private double totalRevenue = 0;
    private double totalCo2 = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Apartment> apartmentsFromFireStore = new ArrayList<Apartment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, null);
        backToProfile = view.findViewById(R.id.backToProfileButton);
        revenueText = view.findViewById(R.id.totalRevenueText);
        co2Text = view.findViewById(R.id.totalCo2Text);

        chart = view.findViewById(R.id.chart);
        getFireBaseData();

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_chartFragment_to_profileFragment);
                onDestroyView();
            }
        });
        return view;
    }

    private ArrayList<Apartment> getFireBaseData() {

        db.getInstance()
                .collection("apartments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            apartmentsFromFireStore = parseInfo(myListOfDocuments);
                            System.out.println("DEBUG: IN apartmentsFromFireBase size: " + apartmentsFromFireStore.size());
                            styleChart();
                            initializeChart(apartmentsFromFireStore);
                        }
                    }
                });
        return apartmentsFromFireStore;
    }

    private ArrayList<Apartment> parseInfo(List<DocumentSnapshot> inputJson) {

        ArrayList<Apartment> apartmentsFromFirebase = new ArrayList<>();
        for (int i = 0; i < inputJson.size(); i++) {
            Apartment aptFromFireStore = new Apartment("", "", "", "", "", "", 0, 0, 0, 0);
            aptFromFireStore.setApartmentId(String.valueOf(i));
            aptFromFireStore.setAddress(inputJson.get(i).getString("address"));
            if (inputJson.get(i).getString("apartmentImageurl") != null) {
                aptFromFireStore.setApartmentImageUrl(inputJson.get(i).getString("apartmentImageurl"));
            } else {
                aptFromFireStore.setApartmentImageUrl("https://source.unsplash.com/random");
            }

            aptFromFireStore.setTenantName(inputJson.get(i).getString("tenantName"));
            aptFromFireStore.setCity(inputJson.get(i).getString("city"));
            aptFromFireStore.setZipCode(inputJson.get(i).getString("zipCode"));
            aptFromFireStore.setArea(inputJson.get(i).getDouble("area"));
            aptFromFireStore.setRent(inputJson.get(i).getDouble("rent"));
            aptFromFireStore.setCo2Amount(inputJson.get(i).getDouble("co2Amount"));
            aptFromFireStore.setResidents((int) Math.round(inputJson.get(i).getDouble("residents")));
            apartmentsFromFirebase.add(aptFromFireStore);
        }
        System.out.println("DEBUG: OUT apartmentsFromFireBase size: " + apartmentsFromFirebase.size());
        return apartmentsFromFirebase;
    }

    private void styleChart() {
        //General chart styling and options
        chart.setNoDataText("No data available");
        chart.setDragEnabled(true);

        //Removing description
        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);

        //Styling legend
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(13f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        chart.setExtraOffsets(0f,0f,0f,15f);

        //Styling X-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(15f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);

        //Styling Y-axis
        YAxis leftAxis = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setAxisMinimum(2f);
        leftAxis.setTextSize(15f);

        //Refresh
        chart.invalidate();
    }

    private BarChart initializeChart(ArrayList<Apartment> apartmentsFromFireStore) {
        ArrayList<BarEntry> rentEntries = new ArrayList<>();
        ArrayList<BarEntry> co2Entries = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();

        //Get the chart values from arraylist of apartments and calculate totals
        for (int i = 0; i<apartmentsFromFireStore.size(); i++) {
            rentEntries.add(new BarEntry(i, (float) apartmentsFromFireStore.get(i).getRent()));
            co2Entries.add(new BarEntry(i, (float) apartmentsFromFireStore.get(i).getCo2Amount()));
            addresses.add(apartmentsFromFireStore.get(i).getAddress());
            totalRevenue = totalRevenue + apartmentsFromFireStore.get(i).getRent();
            totalCo2 = totalCo2 + apartmentsFromFireStore.get(i).getCo2Amount();
        }

        //Set total value texts
        String formattedRevenue = String.format("%.0f€", totalRevenue);
        String formattedCo2 = String.format("%.0fKg CO2e", totalCo2);
        revenueText.setText("Total monthly revenue: " + formattedRevenue);
        co2Text.setText("Total monthly emissions: " + formattedCo2);

        //Initialize bar chart
        BarDataSet revenue = new BarDataSet(rentEntries, "Monthly revenue [€]");
        BarDataSet co2 = new BarDataSet(co2Entries, "Monthly emissions [Kg CO2e]");
        revenue.setColor(Color.BLUE);
        co2.setColor(Color.GREEN);
        revenue.setValueTextSize(15f);
        co2.setValueTextSize(15f);
        BarData data = new BarData(revenue, co2);
        chart.setData(data);

        //Set X-axis values and settings
        float barSpace = 0.1f;
        float groupSpace = 0.5f;
        data.setBarWidth(0.15f);
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace,barSpace) * apartmentsFromFireStore.size());
        chart.groupBars(0, groupSpace, barSpace);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(addresses));
        chart.setVisibleXRangeMaximum(3);

        //Refresh
        chart.invalidate();
        return chart;
    }
}
