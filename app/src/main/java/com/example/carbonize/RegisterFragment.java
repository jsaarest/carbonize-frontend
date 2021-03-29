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

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    EditText mName, mEmail, mPassword;
    Button mRegisterButton;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        mName = v.findViewById(R.id.mFullname);
        mEmail = v.findViewById(R.id.loginEmail);
        mPassword = v.findViewById(R.id.mPassword);
        mRegisterButton = v.findViewById(R.id.registerWithEmail);

        mRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //String name = mName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //check if email contains @ and . and is at least 5 characters long
                if (email.matches("^(?=.*[@])(?=.*[.]).{5,}$")) {
                    System.out.println("email ok");
                }
                else
                {
                    mEmail.setError("Please, provide a valid email");
                    return;
                }

                //check if all requirements for a good password are met
                if (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-_!@#\\$%\\^&\\*]).{12,}$")) {
                    System.out.println("Password ok");
                }
                else{
                    System.out.println("Password " + password + " NOT ok");
                    mPassword.setError("Password must be at least 12 characters long, contain upper and lowercase letters, numbers and special characters");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            System.out.println("User created");
                            // TODO NAVIGATE INSIDE APP DASHBOARD
                            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_dashboardFragment);
                        } else {
                            //one last check if firebase accepted the credentials. If not, the reason is email.
                            System.out.println("ERROR: User creation not successful");
                            mEmail.setError("Invalid email");
                        }
                    }
                });
            }
        });


        return v;
    }







}