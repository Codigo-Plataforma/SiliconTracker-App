package com.silicon.silicontracker;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter  extends RecyclerView.Adapter<DataAdapter.viewHolderRv> {

   ArrayList<String> latitude,longitude,city,pinCode,address;


    public DataAdapter(ArrayList<String> latitude, ArrayList<String> longitude, ArrayList<String> city, ArrayList<String> pinCode, ArrayList<String> address) {

        this.latitude=latitude;
        this.longitude=longitude;
        this.city=city;
        this.pinCode=pinCode;
        this.address=address;
    }


    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public viewHolderRv onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_sample, parent, false);

        return new viewHolderRv(view);


    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.viewHolderRv holder, int position) {

        holder.longitude.setText(longitude.get(position));
        holder.latitude.setText(latitude.get(position));
        holder.city.setText(city.get(position));

    }

    @Override
    public int getItemCount() {
        return longitude.size();
    }

    public class viewHolderRv extends RecyclerView.ViewHolder{
        TextView latitude,longitude,city;

        public viewHolderRv(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);


            latitude=itemView.findViewById(R.id.latitude);
            longitude=itemView.findViewById(R.id.longitude);
            city = itemView.findViewById(R.id.city);

        }
    }

}
