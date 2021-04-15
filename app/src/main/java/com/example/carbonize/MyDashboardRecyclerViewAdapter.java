package com.example.carbonize;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Recylerview adapter for apartment list in dashboard fragment
 *
 */
public class MyDashboardRecyclerViewAdapter extends RecyclerView.Adapter<MyDashboardRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Apartment> apartments;

    public MyDashboardRecyclerViewAdapter(ArrayList<Apartment> apartments) {

        this.apartments = apartments;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item,parent,false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Apartment apartment = apartments.get(position);
        holder.apartmentAddress.setText(apartment.getAddress());
        holder.apartmentOwner.setText(apartment.getTenantName());
        holder.apartmentRent.setText(apartment.getRent() + " €");
        holder.apartmentCo2.setText(String.valueOf(apartment.getCo2Amount()) + " Co2e");
        Picasso.get().load(apartment.getApartmentImageUrl()).into(holder.dashImageView);

    }

    @Override
    public int getItemCount() {
        if (apartments !=null)
        {
            return apartments.size();
        }else
        {
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView apartmentAddress;
        public final TextView apartmentOwner; //TODO change to tenant to match firestore information
        public final TextView apartmentRent;
        public final TextView apartmentCo2;
        public final ImageView dashImageView;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            apartmentAddress = (TextView) view.findViewById(R.id.textAddress);
            apartmentOwner = (TextView) view.findViewById(R.id.textOwner); //TODO change to tenant to match firestore information
            apartmentRent = (TextView) view.findViewById(R.id.textRent);
            apartmentCo2 = (TextView) view.findViewById(R.id.textCo2);
            dashImageView = (ImageView) view.findViewById(R.id.dashImageView);

        }

    }
}