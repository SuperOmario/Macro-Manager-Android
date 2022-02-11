package com.macro_manager.macroapp;

//This code is adapted from https://www.youtube.com/watch?v=18VcnYN5_LM&ab_channel=Stevdza-San

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    JSONArray response;
    Context context;

    public FoodAdapter(Context ct, String response) {
        try {
            this.response = new JSONArray(response);
            System.out.println(this.response.getJSONObject(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        context = ct;
        System.out.println(response);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_row,parent,false);
        return new FoodAdapter.FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        try {
            holder.titleText.setText(response.getJSONObject(position).getString("product_name"));
            holder.calCount.setText(response.getJSONObject(position).getJSONObject("nutriments").getString("energy-kcal_100g"));
            holder.carbCount.setText(response.getJSONObject(position).getJSONObject("nutriments").getString("carbohydrates_100g"));
            holder.fatCount.setText(response.getJSONObject(position).getJSONObject("nutriments").getString("fat_100g"));
            holder.proteinCount.setText(response.getJSONObject(position).getJSONObject("nutriments").getString("proteins_100g"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            System.out.println("LENGTH " + response.length());
            return response.length();
        } catch (Exception e) {
            return 1;
        }
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, calCount, carbCount, fatCount, proteinCount;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.txtTitle);
            calCount = itemView.findViewById(R.id.txtCalCount);
            carbCount = itemView.findViewById(R.id.txtCarbCount);
            fatCount = itemView.findViewById(R.id.txtFatCount);
            proteinCount = itemView.findViewById(R.id.txtProteinCount);
        }
    }
}
