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
    private CircleImageView profileImage;
    private FirebaseAuth mAuth;

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
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutButton(view);
            }
        });
        //TODO set revenue and co2 amounts when methods for calculating them have been created.
        binding.revenueAmount.setText("1234");
        binding.co2Amount.setText("100");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //On logout button press changes back to EnterFragment and logs the user out from the application.
    public void logoutButton(View view) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view_tag, new EnterFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        onDestroyView();
        mAuth.signOut();
        System.out.println("User logged out successfully.");
    }

    //Returns the user to Dashboard
    public void goBackButton(View view) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view_tag, new DashboardFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        onDestroyView();
    }

    //TODO Method to fetch profile image
}