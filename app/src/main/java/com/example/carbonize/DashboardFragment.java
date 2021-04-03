package com.example.carbonize;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A fragment representing a list of Items.
 */
public class DashboardFragment extends Fragment implements Dialog.DialogListener {

    Button addNewButton;
    CircleImageView profileButton;
    TextView totalCo2;
    RecyclerView apartments;
    RecyclerView.Adapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public static ArrayList<Apartment> apartmentsFromFireStore = new ArrayList<Apartment>();

    boolean everythingLoaded = false;

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
        apartmentsToList = getFireBaseData();
        addNewButton = view.findViewById(R.id.addNewLocation);
        profileButton = view.findViewById(R.id.profile_image);
        totalCo2 = view.findViewById(R.id.txtTotalCo2);
        this.apartments = view.findViewById(R.id.rclLocationList);

        RecyclerView.LayoutManager apartmentsLayoutManager = new LinearLayoutManager(getContext());
        this.apartments.setLayoutManager(apartmentsLayoutManager);
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
        System.out.println("Kissat ovat koiria");
    }

    /*
    Add list of apartments to Recylerview
     */
    private ArrayList<Apartment> initApartments(ArrayList<Apartment> allApartments)
    {
        apartmentsFromFireStore.clear();
        adapter.notifyDataSetChanged();
        for (int i=0;i<allApartments.size();i++)
        {
            apartmentsFromFireStore.add(allApartments.get(i));
            //System.out.println("DEBUG: listaan " + allApartments.get(i).getAddress());
        }
        adapter.notifyDataSetChanged();
        return apartmentsFromFireStore;
    }

    /*
        Read apartment data from Firebase Firestore
         */
    private ArrayList<Apartment> getFireBaseData()
    {

        db.getInstance()
                .collection("apartments")
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

    private ArrayList<Apartment> parseInfo(List<DocumentSnapshot> inputJson)  {

        ArrayList<Apartment> apartmentsFromFirebase = new ArrayList<>();
        for (int i=0;i<inputJson.size();i++) {
            Apartment aptFromFireStore= new Apartment("","","","","","",0,0,0,0);
            aptFromFireStore.setApartmentId(String.valueOf(i));
            aptFromFireStore.setAddress(inputJson.get(i).getString("address"));
            if (inputJson.get(i).getString("apartmentImageurl")!=null)
            {
                aptFromFireStore.setApartmentImageUrl(inputJson.get(i).getString("apartmentImageurl"));
            }else
            {
                aptFromFireStore.setApartmentImageUrl("https://source.unsplash.com/random");
            }

            aptFromFireStore.setTenantName(inputJson.get(i).getString("tenantName"));
            aptFromFireStore.setCity(inputJson.get(i).getString("city"));
            aptFromFireStore.setZipCode(inputJson.get(i).getString("zipCode"));
            aptFromFireStore.setArea(inputJson.get(i).getDouble("area"));
            aptFromFireStore.setRent(inputJson.get(i).getDouble("rent"));
            aptFromFireStore.setCo2Amount(inputJson.get(i).getDouble("co2Amount"));
            aptFromFireStore.setResidents((int)Math.round(inputJson.get(i).getDouble("residents")));
            apartmentsFromFirebase.add(aptFromFireStore);

           /*
            System.out.println("DEBUG: " + inputJson.get(i).getString("address"));
            System.out.println("DEBUG: " + inputJson.get(i).getString("apartmentImageurl"));
            System.out.println("DEBUG: " + inputJson.get(i).getString("zipCode"));
            System.out.println("DEBUG: " + inputJson.get(i).getDouble("area"));
            System.out.println("DEBUG: " + inputJson.get(i).getDouble("rent"));
            System.out.println("DEBUG: " + inputJson.get(i).getDouble("residents"));

            */
        }
        //System.out.println("DEBUG: OUT apartmentsFromFireBase size: " + apartmentsFromFirebase.size());
        initApartments(apartmentsFromFirebase);
        return apartmentsFromFirebase;
    }

    public ArrayList<Apartment> getApartments() {
        return apartmentsFromFireStore;
    }
}