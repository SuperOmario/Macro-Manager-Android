package Adapters;

//This code is adapted from https://www.youtube.com/watch?v=18VcnYN5_LM&ab_channel=Stevdza-San and https://www.youtube.com/watch?v=5YFPkFaLcIo&ab_channel=MickeyFaisal

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.macro_manager.macroapp.FoodDetails;
import Listeners.FoodListener;
import com.macro_manager.macroapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    JSONArray response;
    Context context;
    String recipe;
    FoodListener foodListener;
    ArrayList<Integer> foodIDs = new ArrayList<Integer>();

    public FoodAdapter(Context ct, String response, String recipe, FoodListener foodListener) {
        try {
            this.response = new JSONArray(response);
            System.out.println(this.response.getJSONObject(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        context = ct;
        this.recipe = recipe;
        this.foodListener = foodListener;
        System.out.println(response);
    }

    private void base(FoodViewHolder holder, int position) {
        try {
            holder.titleText.setText(response.getJSONObject(position).getString("product_name"));
            holder.calCount.setText(String.valueOf(response.getJSONObject(position).getJSONObject("nutriments").getInt("energy-kcal_100g")));
            holder.carbCount.setText(String.valueOf(response.getJSONObject(position).getJSONObject("nutriments").getInt("carbohydrates_100g")));
            holder.fatCount.setText(String.valueOf(response.getJSONObject(position).getJSONObject("nutriments").getInt("fat_100g")));
            holder.proteinCount.setText(String.valueOf(response.getJSONObject(position).getJSONObject("nutriments").getInt("proteins_100g")));
            holder.idText.setText(String.valueOf(response.getJSONObject(position).getInt("IngredientID")));
            holder.serving.setText(response.getJSONObject(position).getString("serving_quantity"));
            holder.barcode.setText(response.getJSONObject(position).getJSONObject("Barcode").getString("Valid"));
            switch (recipe) {
                case "New" :
                case "Update" :
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (holder.checkBox.isChecked()) {
                                try {
                                    foodIDs.add(response.getJSONObject(position).getInt("IngredientID"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    foodIDs.removeAll(Arrays.asList(response.getJSONObject(position).getInt("IngredientID")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            foodListener.onFoodChange(foodIDs);
                        }
                    });

                    break;

                    default :
                            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                            Intent intent = new Intent(context, FoodDetails.class);
                            intent.putExtra("title", holder.titleText.getText());
                            intent.putExtra("calories", holder.calCount.getText());
                            intent.putExtra("carbs", holder.carbCount.getText());
                            intent.putExtra("fat", holder.fatCount.getText());
                            intent.putExtra("protein", holder.proteinCount.getText());
                            intent.putExtra("id", holder.idText.getText());
                            intent.putExtra("servingSize", holder.serving.getText());
                            intent.putExtra("barcode", holder.barcode.getText());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);


                    }
                });
                            break;
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (response != null) {
            base(holder,position);
        } else {
            holder.mainLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(context.getApplicationContext(), "You have no food entries, make some!", Toast.LENGTH_SHORT).show();
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

        TextView titleText, calCount, carbCount, fatCount, proteinCount, idText, serving, barcode;
        ConstraintLayout mainLayout;
        CheckBox checkBox;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.txtTitle);
            calCount = itemView.findViewById(R.id.txtCalCount);
            carbCount = itemView.findViewById(R.id.txtCarbCount);
            fatCount = itemView.findViewById(R.id.txtFatCount);
            proteinCount = itemView.findViewById(R.id.txtProteinCount);
            idText = itemView.findViewById(R.id.txtRecipeID);
            serving = itemView.findViewById(R.id.txtServingSizeRecipe);
            barcode = itemView.findViewById(R.id.txtBarcode);
            checkBox = itemView.findViewById(R.id.checkBox);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
