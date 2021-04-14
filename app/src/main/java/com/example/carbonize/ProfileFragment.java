package com.example.carbonize;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carbonize.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;




public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    DashboardFragment dashboard = new DashboardFragment();
    private double revenue = dashboard.totalRevenue;
    private double co2 = dashboard.totalCarbon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //On logout button press resets this fragment, moves the user to Enter fragment, and signs them out through firebase.
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_enterFragment);
                onDestroyView();
                mAuth.signOut();
                System.out.println("User logged out successfully.");
            }
        });

        //On back button press takes user back to dashboard and resets this fragment.
        binding.backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_dashboardFragment);
                onDestroyView();
            }
        });

        //On graph button resets this fragment and takes the user to the graph view
        binding.graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_chartFragment);
                onDestroyView();
            }
        });

        String formattedRevenue = String.format("%.0f€", revenue);
        String formattedCo2 = String.format("%.0f", co2).replace(".", ",");
        binding.revenueAmount.setText(formattedRevenue);
        binding.co2Amount.setText(formattedCo2 + " Kg CO2e");
        return view;
    }

    //Method to reset the fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}