package com.macro_manager.macroapp;

//This code is adapted from https://www.youtube.com/watch?v=18VcnYN5_LM&ab_channel=Stevdza-San

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{

    JSONArray response;
    Context context;

    public IngredientAdapter(Context ct, String response) {
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
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_row,parent,false);
        return new IngredientAdapter.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        try {
            holder.titleText.setText(response.getJSONObject(position).getString("product_name"));
            holder.calCount.setText(String.valueOf(response.getJSONObject(position).getJSONObject("nutriments").getInt("energy-kcal_100g")));
            holder.carbCount.setText(String.valueOf(response.getJSONObject(position).getJSONObject("nutriments").getInt("carbohydrates_100g")));
            holder.fatCount.setText(String.valueOf(response.getJSONObject(position).getJSONObject("nutriments").getInt("fat_100g")));
            holder.proteinCount.setText(String.valueOf(response.getJSONObject(position).getJSONObject("nutriments").getInt("proteins_100g")));
            holder.idText.setText(String.valueOf(response.getJSONObject(position).getInt("IngredientID")));
            holder.serving.setText(response.getJSONObject(position).getString("serving_quantity"));

            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, IngredientDetails.class);
                    intent.putExtra("title", holder.titleText.getText());
                    intent.putExtra("calories", holder.calCount.getText());
                    intent.putExtra("carbs", holder.carbCount.getText());
                    intent.putExtra("fat", holder.fatCount.getText());
                    intent.putExtra("protein", holder.proteinCount.getText());
                    intent.putExtra("id", holder.idText.getText());
                    intent.putExtra("servingSize", holder.serving.getText());
                    context.startActivity(intent);
                }
            });
            holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
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

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, calCount, carbCount, fatCount, proteinCount, idText, serving;
        ConstraintLayout mainLayout;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.txtTitle);
            calCount = itemView.findViewById(R.id.txtCalCount);
            carbCount = itemView.findViewById(R.id.txtCarbCount);
            fatCount = itemView.findViewById(R.id.txtFatCount);
            proteinCount = itemView.findViewById(R.id.txtProteinCount);
            idText = itemView.findViewById(R.id.txtID);
            serving = itemView.findViewById(R.id.txtServing);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
