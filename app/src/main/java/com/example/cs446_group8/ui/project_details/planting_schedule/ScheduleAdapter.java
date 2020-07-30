package com.example.cs446_group8.ui.project_details.planting_schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cs446_group8.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.CropViewHolder> {
    //public List crops =

    public class CropViewHolder extends RecyclerView.ViewHolder {
        public TextView cropName, amountToPlant;

        public CropViewHolder(View view) {
            super(view);
            cropName = view.findViewById(R.id.crop_name);
            amountToPlant = view.findViewById(R.id.crop_amount);
        }
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_item_view, parent, false);
        return new CropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        //cropslist.get(position)
        //todo replace with actual values from crop info
        holder.cropName.setText("Artichoke");
        holder.amountToPlant.setText("2.75");
    }

    @Override
    public int getItemCount() { return 10; }

}