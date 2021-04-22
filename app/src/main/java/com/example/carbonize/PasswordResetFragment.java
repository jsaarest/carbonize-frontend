package com.example.carbonize;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetFragment extends Fragment {

    public PasswordResetFragment() {
        // Required empty public constructor
    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String emailAddress = "";
    EditText mEmail;
    Button mSendResetLinkBtn, backToLoginFragmentBtn;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_password_reset, container, false);
        mEmail = v.findViewById(R.id.passwordResetEmail);
        mSendResetLinkBtn = v.findViewById(R.id.passWordResetBtn);
        backToLoginFragmentBtn = v.findViewById(R.id.backToLoginFragmentBtn);

        //On back button press takes user back to login screen.
        backToLoginFragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_passwordResetFragment_to_loginFragment);
            }
        });

        mSendResetLinkBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = mEmail.getText().toString().trim();

                //check if email contains @ and . and is at least 5 characters long
                if (email.matches("^(?=.*[@])(?=.*[.]).{5,}$")) {
                    System.out.println("email ok");
                }
                else
                {
                    mEmail.setError("Please, provide a valid email");
                    return;
                }


                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Success", "Email sent.");
                                    mSendResetLinkBtn.setText("Reset link sent!");
                                    mEmail.setText("");
                                }
                            }
                        });
            }
        });
        return v;
    }
}