package com.example.carbonize;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EnterFragment extends Fragment {
    Button goToLoginPage;
    Button goToRegisterPage;
    private String currentUser;

    public EnterFragment(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Error handling to prevent null object exception with Android Studio 4.1.3
        try{
            currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception e) {
            e.printStackTrace();
            currentUser = "";
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enter, container, false);
        goToLoginPage = view.findViewById(R.id.goToLoginView);
        goToRegisterPage = view.findViewById(R.id.registerButton);

        // If previous log in is found, redirect straight to dashboard
        if (currentUser != null) {
            // User is signed in
            Log.d("info", "AuthState: User found: " + currentUser);
            NavHostFragment.findNavController(this).navigate(R.id.action_enterFragment_to_dashboardFragment, null);
        } else {
            // User is signed out
            goToLoginPage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_enterFragment_to_loginFragment, null));
            Log.d("info", "AuthState: NoUser");
        }


        goToRegisterPage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_enterFragment_to_registerFragment, null));
        return view;

    }





}