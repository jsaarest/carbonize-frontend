package com.example.carbonize;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.util.ArrayList;

public class BarChartFrag extends AppCompatDialogFragment {
        private DialogListener listener;
        private BarChart chart;
        private ImageView image;

        @NonNull
        @Override
        public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_statistic, null);
            builder.setView(view);
            chart = view.findViewById(R.id.chartImage);
            return builder.create();
        }


        public interface DialogListener {
            void printMessage();
        }

        public void initializeData() {
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0f, 30f));
            entries.add(new BarEntry(1f, 80f));
            entries.add(new BarEntry(2f, 60f));
            entries.add(new BarEntry(3f, 50f));
            // gap of 2f
            entries.add(new BarEntry(5f, 70f));
            entries.add(new BarEntry(6f, 60f));
            BarDataSet set = new BarDataSet(entries, "BarDataSet");
            BarData data = new BarData(set);
            data.setBarWidth(0.9f); // set custom bar width
            chart.setData(data);
            chart.setFitBars(true); // make the x-axis fit exactly all bars
            image.set
            //chart.invalidate(); // refresh
        }


}
