package com.example.carbonize.UI;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carbonize.CarbonAndRevenueCalculator;
import com.example.carbonize.ImageRandomizer;
import com.example.carbonize.R;
import com.example.carbonize.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


@RequiresApi(api = Build.VERSION_CODES.R)
public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    CarbonAndRevenueCalculator calculator = CarbonAndRevenueCalculator.getInstance();
    private double revenue;
    private double co2;

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

        //Check if user has an email and call a pseudorandom image for their profile image
        if (FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null)
        {
            String pseudoRndProfileImage = new ImageRandomizer().getRandomProfileImage(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
            Picasso.get().load("https://picsum.photos/id/"+ pseudoRndProfileImage + "/300/300").noFade().fit().into(binding.profileImage);
        }
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

        getTotalRevenueAndCo2();
        return view;
    }

    //Method to reset the fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Gets the total revenue and Co2 amounts from CarbonAndRevenueCalculator class, formats them to desired format, and updates the texts in the interface.
    private void getTotalRevenueAndCo2() {
        calculator.calculateTotalRevenueAndCo2();
        revenue = CarbonAndRevenueCalculator.totalRevenue;
        co2 = CarbonAndRevenueCalculator.totalCo2;
        String formattedRevenue = String.format("%.1f €", revenue).replace(".", ",");
        String formattedCo2 = String.format("%.1f", co2).replace(".", ",");
        binding.revenueAmount.setText(formattedRevenue);
        binding.co2Amount.setText(formattedCo2 + " kg CO2e");
    }
}