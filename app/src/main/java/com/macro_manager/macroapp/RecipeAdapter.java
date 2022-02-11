package com.macro_manager.macroapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

//This code is adapted from https://www.youtube.com/watch?v=18VcnYN5_LM&ab_channel=Stevdza-San

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    JSONArray response;
    Context context;

    public RecipeAdapter(Context ct, String response) {
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
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_row,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        try {
            holder.titleText.setText(response.getJSONObject(position).getString("Title"));
            holder.calCount.setText(response.getJSONObject(position).getString("Calories"));
            holder.carbCount.setText(response.getJSONObject(position).getString("Carbohydrate"));
            holder.fatCount.setText(response.getJSONObject(position).getString("Fat"));
            holder.proteinCount.setText(response.getJSONObject(position).getString("Protein"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            System.out.println(response.length());
            return response.length();
        } catch (Exception e) {
            return 1;
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, calCount, carbCount, fatCount, proteinCount;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.txtTitle);
            calCount = itemView.findViewById(R.id.txtCalCount);
            carbCount = itemView.findViewById(R.id.txtCarbCount);
            fatCount = itemView.findViewById(R.id.txtFatCount);
            proteinCount = itemView.findViewById(R.id.txtProteinCount);
        }
    }
}
