package com.example.carbonize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carbonize.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    //https://medium.com/androiddevelopers/use-view-binding-to-replace-findviewbyid-c83942471fc
    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private double revenue = 1234;
    private double co2 = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
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

        binding.graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChartDialog();
            }
        });

        //TODO set revenue and co2 amounts when methods for calculating them have been created.
        String formattedRevenue = String.format("%.0f€", revenue);
        String formattedCo2 = String.format("%.1f", co2).replace(".", ",");
        binding.revenueAmount.setText(formattedRevenue);
        binding.co2Amount.setText(formattedCo2 + " Kg CO2e");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void openChartDialog() {
        Dialog dialog = new Dialog();
        dialog.show(getParentFragmentManager(), "BarChartFrag");
    }
}