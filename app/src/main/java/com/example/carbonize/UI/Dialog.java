package com.example.carbonize.UI;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.carbonize.R;


public class Dialog extends AppCompatDialogFragment {
    private DialogListener listener;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view);

        return builder.create();
    }


    public interface DialogListener {
        void printMessage();
    }


}
