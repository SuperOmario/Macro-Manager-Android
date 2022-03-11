package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.macro_manager.macroapp.R;

import org.json.JSONArray;
import org.json.JSONException;

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
        if (response != null) {
            try {
                holder.titleText.setText(response.getJSONObject(position).getString("Date").substring(0,10));
                holder.calCount.setText(String.valueOf(response.getJSONObject(position).getInt("Calories")));
                holder.fatCount.setText(String.valueOf(response.getJSONObject(position).getInt("Fat")));
                holder.carbCount.setText(String.valueOf(response.getJSONObject(position).getInt("Carbohydrate")));
                holder.proteinCount.setText(String.valueOf(response.getJSONObject(position).getInt("Protein")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.mainLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(context.getApplicationContext(), "You have no diary entries, make some!", Toast.LENGTH_SHORT).show();
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

        TextView titleText, calCount, carbCount, fatCount, proteinCount, idText, serving;
        ConstraintLayout mainLayout;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.txtTitle);
            calCount = itemView.findViewById(R.id.txtCalCount);
            carbCount = itemView.findViewById(R.id.txtCarbCount);
            fatCount = itemView.findViewById(R.id.txtFatCount);
            proteinCount = itemView.findViewById(R.id.txtProteinCount);
            idText = itemView.findViewById(R.id.txtRecipeID);
            serving = itemView.findViewById(R.id.txtServingSizeRecipe);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
