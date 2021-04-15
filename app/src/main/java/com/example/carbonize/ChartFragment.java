package com.example.carbonize;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.carbonize.databinding.FragmentChartBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;


@RequiresApi(api = Build.VERSION_CODES.R)
public class ChartFragment extends Fragment {
    private FragmentChartBinding binding;
    private BarChart chart;
    DashboardFragment dashboard = new DashboardFragment();
    private double totalRevenue = dashboard.totalRevenue;
    private double totalCo2 = dashboard.totalCarbon;
    private static ArrayList<Apartment> apartments = new ArrayList<Apartment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        chart = binding.chart;

        //Initializing apartments list with data fetched in dashboard
        apartments = dashboard.apartmentsFromFireStore;

        //Styling and creating the barchart
        styleChart();
        initializeChart(apartments);

        binding.backToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_chartFragment_to_profileFragment);
                onDestroyView();
            }
        });
        return view;
    }

    //General styling of the barchart
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

    //Initializing barchart data and axis values
    private void initializeChart(ArrayList<Apartment> apartmentsFromFireStore) {
        ArrayList<BarEntry> rentEntries = new ArrayList<>();
        ArrayList<BarEntry> co2Entries = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();
        String streetName;

        //Get the chart values from arraylist of apartments
        for (int i = 0; i<apartmentsFromFireStore.size(); i++) {
            rentEntries.add(new BarEntry(i, (float) apartments.get(i).getRent()));
            co2Entries.add(new BarEntry(i, (float) apartments.get(i).getCo2Amount()));
            streetName = apartments.get(i).getAddress();
            //Method to limit the X axis label name length to prevent overlapping due to the MPAndroidChart library not supporting multi-lined labels.
            if (streetName.length() > 19) {
                String[] splitName = streetName.split("(?<=\\G.{19})");
                addresses.add(splitName[0]);
            }
            else {
                addresses.add(apartments.get(i).getAddress());
            }
        }

        //Set total value texts
        String formattedRevenue = String.format("%.0f€", totalRevenue);
        String formattedCo2 = String.format("%.0f Kg CO2e", totalCo2);
        binding.totalRevenueText.setText("Total monthly revenue: " + formattedRevenue);
        binding.totalCo2Text.setText("Total monthly emissions: " + formattedCo2);

        //Initialize bar chart
        BarDataSet revenue = new BarDataSet(rentEntries, "Monthly revenue [€]");
        BarDataSet co2 = new BarDataSet(co2Entries, "Monthly emissions [kg CO2e]");
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
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace,barSpace) * apartments.size());
        chart.groupBars(0, groupSpace, barSpace);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(addresses));
        chart.setVisibleXRangeMaximum(2);

        //Refresh
        chart.invalidate();
    }
}
