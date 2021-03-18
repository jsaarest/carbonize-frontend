package com.example.carbonize;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.IOException;
import java.util.Objects;

public class Dialog extends AppCompatDialogFragment {
    EditText amount;
    private DialogListener listener;
    Process process;
    Button shareButton;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        shareButton = view.findViewById(R.id.shareButton);
        builder.setView(view);

        return builder.create();
    }


    public interface DialogListener {
        void printMessage();
    }


}
