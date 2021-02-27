package com.example.carbonize;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class EnterFragment extends Fragment {
    Button goToLoginPage;

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
        goToLoginPage = view.findViewById(R.id.buttonLogin);
        //goToLoginPage.setOnClickListener(this);
        goToLoginPage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_enterFragment_to_loginFragment, null));
        return view;

    }


    /*@Override
    public void onClick(View view) {
        Navigation.findNavController(view).navigate(view.findViewById(R.id.action_enterFragment_to_loginFragment));
    }*/

}