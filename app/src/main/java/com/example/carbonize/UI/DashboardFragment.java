package com.example.carbonize.UI;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.TextView;

import com.example.carbonize.Apartment;
import com.example.carbonize.CarbonAndRevenueCalculator;
import com.example.carbonize.ImageRandomizer;
import com.example.carbonize.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    CarbonAndRevenueCalculator calculator = CarbonAndRevenueCalculator.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static ArrayList<Apartment> apartmentsFromFireStore = new ArrayList<Apartment>();

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

        //Check if user has an email and call a pseudorandom image for their profile image
        if (FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null)
        {
            String pseudoRndProfileImage = new ImageRandomizer().getRandomProfileImage(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
            Picasso.get().load("https://picsum.photos/id/"+ pseudoRndProfileImage + "/300/300").noFade().fit().into(profileButton);
        }


        // Swipe handler for deleting objects
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // When item is swiped, remove item from arrayList and db
                apartmentsFromFireStore.sort(new CreatedAtSorter()); // Sort the apartments by createdAt timestamp
                Apartment deletedApartment = apartmentsFromFireStore.get(viewHolder.getAdapterPosition());

                db.collection("apartments").document(deletedApartment.getApartmentId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        apartmentsToList.remove(viewHolder.getAdapterPosition());
                        //also remove from list that is base for calculations
                        apartmentsFromFireStore.remove(viewHolder.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        adapter.notifyDataSetChanged();
                        //update co2 and revenue after deletion
                        getTotalRevenueAndCo2();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERR", "Error deleting document", e);
                    }
                });
            }
        }).attachToRecyclerView(apartments);



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
    public class CreatedAtSorter implements Comparator<Apartment>
        // This class sorts apartments by createdAt timestamp
    {
        @Override
        public int compare(Apartment a1, Apartment a2) {
            return a2.getCreatedAt().compareTo(a1.getCreatedAt());
        }
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
            //add each apartment on the list
            apartmentsFromFireStore.add(allApartments.get(i));
        }
        apartmentsFromFireStore.sort(new CreatedAtSorter()); // Sort the apartments by createdAt timestamp
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
                            //After the apartments have been successfully fetched, calculator class can use the arraylist to calculate total revenue and Co2 amounts.
                            getTotalRevenueAndCo2();
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
        ArrayList<Apartment> apartmentsFromFirebase = new ArrayList<>();
        for (int i=0;i<inputJson.size();i++) {
            Apartment aptFromFireStore= new Apartment("","","","","","","",0,0,0, 0, 0);
            aptFromFireStore.setApartmentId(String.valueOf(i));
            aptFromFireStore.setApartmentId(inputJson.get(i).getReference().getId());
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
            aptFromFireStore.setRent(doubleRound(inputJson.get(i).getDouble("rent"), 1));
            aptFromFireStore.setCreatedAt(inputJson.get(i).getLong("createdAt"));


            aptFromFireStore.setCo2Amount(doubleRound(inputJson.get(i).getDouble("co2Amount"),1));

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

    public static double doubleRound (double value, int precision) {
        /*
        tool method to round a double to wanted precision
        returns rounded double
         */
        int scale = (int)Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    //Gets the total revenue and Co2 amounts from CarbonAndRevenueCalculator class, formats them to desired format, and updates the texts in the interface.
    private void getTotalRevenueAndCo2() {
        calculator.calculateTotalRevenueAndCo2();
        String formattedRevenue = String.format("%.1f €", CarbonAndRevenueCalculator.totalRevenue).replace(".", ",");
        String formattedCo2 = String.format("%.1f", CarbonAndRevenueCalculator.totalCo2).replace(".", ",") + " kg CO2e";
        totalCo2.setText(formattedCo2);
        totalEur.setText(formattedRevenue);
    }
}