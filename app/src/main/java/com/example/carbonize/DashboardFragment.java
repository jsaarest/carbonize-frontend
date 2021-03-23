package com.example.carbonize;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.carbonize.dummy.DummyContent;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A fragment representing a list of Items.
 */
public class DashboardFragment extends Fragment {

    Button addNewButton;
    CircleImageView profileButton;
    RecyclerView apartments;
    RecyclerView.Adapter adapter;


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;


    ArrayList<Apartment> apartmentsToList;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public DashboardFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DashboardFragment newInstance(int columnCount) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        addNewButton = view.findViewById(R.id.addNewLocation);
        profileButton = view.findViewById(R.id.profile_image);
        this.apartments = view.findViewById(R.id.rclLocationList);
        apartmentsToList = initApartments();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        this.apartments.setLayoutManager(mLayoutManager);
        adapter = new MyDashboardRecyclerViewAdapter(apartmentsToList);
        this.apartments.setAdapter(adapter);



        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_dashboardFragment_to_addApartment);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_dashboardFragment_to_profileFragment);
            }
        });


        return view;
    }
    private ArrayList<Apartment> initApartments(){
        ArrayList<Apartment> aptList = new ArrayList<Apartment>();
        //apartmentsToList.clear();
        //TODO: Add actual apartments from Firebase
        aptList.add(new Apartment("0","Vuorikatu 2", "00100", "Helsinki", "Pasi Matti", "https://www.supersaa.fi/assets/ver-1613639598881/images/weather-icons/yo1.png", 1, 123.3, 65, 650));
        aptList.add(new Apartment("0","Vuorikatu 2", "00100", "Helsinki", "Pasi Matti", "https://www.supersaa.fi/assets/ver-1613639598881/images/weather-icons/yo1.png", 1, 123.3, 65, 650));
        aptList.add(new Apartment("0","Vuorikatu 2", "00100", "Helsinki", "Pasi Matti", "https://www.supersaa.fi/assets/ver-1613639598881/images/weather-icons/yo1.png", 1, 123.3, 65, 650));
        aptList.add(new Apartment("0","Vuorikatu 2", "00100", "Helsinki", "Pasi Matti", "https://www.supersaa.fi/assets/ver-1613639598881/images/weather-icons/yo1.png", 1, 123.3, 65, 650));
        for (int i=0;i<aptList.size();i++)
        {
            System.out.println("DEBUG:" + aptList.get(i).getAddress());
        }

    return aptList;
    }
}