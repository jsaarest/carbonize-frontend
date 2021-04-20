package com.example.carbonize;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A fragment representing a list of Apartment Items.
 */
@RequiresApi(api = Build.VERSION_CODES.R)
public class DashboardFragment extends Fragment implements Dialog.DialogListener {

    Button addNewButton;
    CircleImageView profileButton;
    TextView totalCo2, totalEur;

    RecyclerView apartments;
    RecyclerView.Adapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public static ArrayList<Apartment> apartmentsFromFireStore = new ArrayList<Apartment>();

    //public variables for other fragments
    public static double totalRevenue =0;
    public static double totalCarbon =0;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;


    ArrayList<Apartment> apartmentsToList;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public DashboardFragment() {
    }


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
        apartmentsToList = getFireBaseData();
        addNewButton = view.findViewById(R.id.addNewLocation);
        profileButton = view.findViewById(R.id.profile_image);
        totalCo2 = view.findViewById(R.id.txtTotalCo2);
        totalEur = view.findViewById(R.id.txtTotalRent);


        this.apartments = view.findViewById(R.id.rclLocationList);

        RecyclerView.LayoutManager apartmentsLayoutManager = new LinearLayoutManager(getContext());
        this.apartments.setLayoutManager(apartmentsLayoutManager);
        adapter = new MyDashboardRecyclerViewAdapter(apartmentsToList);
        this.apartments.setAdapter(adapter);

        /*
        Add onClickListeners to all clickable UI-items:
        - UserProfile image
        - Add new listing button
        - Total Co2 -label
         */
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

        totalCo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        return view;
    }

    public void openDialog(){
        Dialog dialog = new Dialog();
        dialog.show(getParentFragmentManager(), "dialog");
    }

    public void printMessage(){
        System.out.println("Message printed");
    }

    private ArrayList<Apartment> initApartments(ArrayList<Apartment> allApartments)
     /*
    Add list of apartments to Recylerview
     */
    {
        apartmentsFromFireStore.clear();
        adapter.notifyDataSetChanged();
        for (int i=0;i<allApartments.size();i++)
        {
            apartmentsFromFireStore.add(allApartments.get(i));

        }
        adapter.notifyDataSetChanged();
        return apartmentsFromFireStore;
    }

    /*
        Read apartment data from Firebase Firestore
    */
    private ArrayList<Apartment> getFireBaseData()
    /*
    Read Firebase database for apartment information
    returns ArrayList<Apartment> of all apartments in firestore
     */
    {

        db.getInstance()
                .collection("apartments")
                .whereEqualTo("owner", currentUser ) // Only query documents that belong to user
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            apartmentsFromFireStore = parseInfo(myListOfDocuments);
                            //System.out.println("DEBUG: IN apartmentsFromFireBase size: " + apartmentsFromFireStore.size());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        return apartmentsFromFireStore;
    }


    private ArrayList<Apartment> parseInfo(List<DocumentSnapshot> inputJson){
    /*
    Parse apartment data ready to be added to the Recyclerview
    Method returns user's every apartment in an ArrayList<Apartment>
     */
        totalCarbon=0;
        totalRevenue=0;
        ArrayList<Apartment> apartmentsFromFirebase = new ArrayList<>();
        for (int i=0;i<inputJson.size();i++) {
            Apartment aptFromFireStore= new Apartment("","","","","","","",0,0,0, 0, 0);
            aptFromFireStore.setApartmentId(String.valueOf(i));
            aptFromFireStore.setAddress(inputJson.get(i).getString("address"));
            if (inputJson.get(i).getString("apartmentImageurl")!=null)
            {
                aptFromFireStore.setApartmentImageUrl(inputJson.get(i).getString("apartmentImageurl"));
            }else
            {
                String rndImg = new ImageRandomizer().getRandomApartmentImage();
                aptFromFireStore.setApartmentImageUrl("https://picsum.photos/id/"+rndImg+"/300/300");
            }

            aptFromFireStore.setTenantName(inputJson.get(i).getString("tenantName"));
            aptFromFireStore.setCity(inputJson.get(i).getString("city"));
            aptFromFireStore.setZipCode(inputJson.get(i).getString("zipCode"));
            aptFromFireStore.setArea(inputJson.get(i).getDouble("area"));
            aptFromFireStore.setRent(inputJson.get(i).getDouble("rent"));

            //add rent euros to total revenue amount to be added to UI
            totalRevenue += doubleRound(inputJson.get(i).getDouble("rent"),1);
            totalEur.setText(String.valueOf(totalRevenue) + " €");

            aptFromFireStore.setCo2Amount(doubleRound(inputJson.get(i).getDouble("co2Amount"),1));

            //add co2e to total carbon amount to be added to UI
            totalCarbon += doubleRound(inputJson.get(i).getDouble("co2Amount"),1);
            totalCo2.setText(String.valueOf(totalCarbon) + " kg CO2e");
            aptFromFireStore.setResidents((int)Math.round(inputJson.get(i).getDouble("residents")));
            apartmentsFromFirebase.add(aptFromFireStore);

        }

        initApartments(apartmentsFromFirebase);
        return apartmentsFromFirebase;
    }


    public ArrayList<Apartment> getApartments() {
        /*
        Helper method that returns ArrayList<Apartment> that holds current apartments in Firestore
        used for instance to pass Arraylist to BarChart creation
         */
        return apartmentsFromFireStore;
    }


    private static double doubleRound (double value, int precision) {
        /*
        tool method to round a double to wanted precision
        returns rounded double
         */
        int scale = (int)Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}