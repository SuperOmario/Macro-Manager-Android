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
import org.json.JSONObject;

//This code is adapted from https://www.youtube.com/watch?v=18VcnYN5_LM&ab_channel=Stevdza-San

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    JSONArray response;
    Context context;

    public DiaryAdapter(Context ct, String response) {
        try {
            this.response = new JSONArray(response);
            System.out.println(this.response.getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        context = ct;
        System.out.println(response);
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.diary_row, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        try {
            holder.titleText.setText(response.getJSONObject(position).getString("Date"));
            holder.meal.setText(response.getJSONObject(position).getString("Meal"));
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

    public class DiaryViewHolder extends RecyclerView.ViewHolder{

        TextView titleText, meal;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            meal = itemView.findViewById(R.id.meal);
        }
    }
}
