package com.example.carbonize;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class EnterFragment extends Fragment {
    Button goToLoginPage;
    Button goToRegisterPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enter, container, false);
        goToLoginPage = view.findViewById(R.id.goToLoginView);
        goToRegisterPage = view.findViewById(R.id.registerButton);
        goToLoginPage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_enterFragment_to_loginFragment, null));
        goToRegisterPage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_enterFragment_to_registerFragment, null));
        return view;

        // kommentti

    }





}