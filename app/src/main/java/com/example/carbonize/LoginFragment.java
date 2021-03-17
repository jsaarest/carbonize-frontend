package com.example.carbonize;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    EditText mEmail, mPassword;
    Button mLoginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        mEmail = v.findViewById(R.id.loginEmail);
        mPassword = v.findViewById(R.id.loginPassword);
        mLoginButton = v.findViewById(R.id.loginWithEmail);

        // Add some logic to login button
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Please, provide a email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Please, provide a password");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            System.out.println("Logged in");
                            // Navigate inside app dashboard
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_dashboardFragment);
                        } else {

                            System.out.println("ERROR: Login not successful");
                            //TODO Handle errors with failed login
                        }
                    }
                });
            }
        });


        return v;
    }
}