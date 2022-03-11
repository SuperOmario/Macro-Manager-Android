package Adapters;

import android.annotation.SuppressLint;
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

import com.macro_manager.macroapp.R;
import com.macro_manager.macroapp.Recipe;
import com.macro_manager.macroapp.RecipeDetails;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

import Listeners.RecipeListener;

//This code is adapted from https://www.youtube.com/watch?v=18VcnYN5_LM&ab_channel=Stevdza-San

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    JSONArray response;
    Context context;
    String category;
    RecipeListener recipeListener;

    ArrayList<Integer> recipeIDs = new ArrayList<>();

    public RecipeAdapter(Context ct, String response, String category, RecipeListener recipeListener) {
        try {
            this.response = new JSONArray(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context = ct;
        this.category = category;
        this.recipeListener = recipeListener;
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
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (response != null) {
                try {
                    holder.titleText.setText(response.getJSONObject(position).getString("Title"));
                    holder.idText.setText(response.getJSONObject(position).getString("RecipeID"));
                    holder.serving.setText(response.getJSONObject(position).getString("ServingSize"));
                    holder.calCount.setText(String.valueOf(response.getJSONObject(position).getInt("Calories")));
                    holder.carbCount.setText(String.valueOf(response.getJSONObject(position).getInt("Carbohydrate")));
                    holder.fatCount.setText(String.valueOf(response.getJSONObject(position).getInt("Fat")));
                    holder.proteinCount.setText(String.valueOf(response.getJSONObject(position).getInt("Protein")));
                    holder.idText.setText(response.getJSONObject(position).getString("RecipeID"));
                    holder.serving.setText(response.getJSONObject(position).getString("ServingSize"));
                    switch (category) {
                        case "New" :
                        case "Update" :
                           
                            holder.checkBox.setVisibility(View.VISIBLE);
                            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (holder.checkBox.isChecked()) {
                                        try {
                                            recipeIDs.add(response.getJSONObject(position).getInt("RecipeID"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            recipeIDs.removeAll(Arrays.asList(response.getJSONObject(position).getInt("RecipeID")));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    recipeListener.onRecipeChange(recipeIDs);
                                }
                            });
                            break;
                        default :
                            holder.calCount.setText(String.valueOf(response.getJSONObject(position).getInt("Calories")));
                            holder.carbCount.setText(String.valueOf(response.getJSONObject(position).getInt("Carbohydrate")));
                            holder.fatCount.setText(String.valueOf(response.getJSONObject(position).getInt("Fat")));
                            holder.proteinCount.setText(String.valueOf(response.getJSONObject(position).getInt("Protein")));
                            holder.idText.setText(response.getJSONObject(position).getString("RecipeID"));
                            holder.serving.setText(response.getJSONObject(position).getString("ServingSize"));

                            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, RecipeDetails.class);
                                    intent.putExtra("title", holder.titleText.getText());
                                    intent.putExtra("calories", holder.calCount.getText());
                                    intent.putExtra("carbs", holder.carbCount.getText());
                                    intent.putExtra("fat", holder.fatCount.getText());
                                    intent.putExtra("protein", holder.proteinCount.getText());
                                    intent.putExtra("id", holder.idText.getText());
                                    intent.putExtra("servingSize", holder.serving.getText());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    holder.titleText.setText(response.getJSONObject(position).getString("Title"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        TextView titleText, calCount, calCountt, carbCount, carbCountt, fatCount, fatCountt, proteinCount, proteintCountt, idText, serving;
        ConstraintLayout mainLayout;
        CheckBox checkBox;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.txtTitle);
            calCount = itemView.findViewById(R.id.txtCalCount);
            calCountt = itemView.findViewById(R.id.textView4);
            carbCount = itemView.findViewById(R.id.txtCarbCount);
            carbCountt = itemView.findViewById(R.id.textView5);
            fatCount = itemView.findViewById(R.id.txtFatCount);
            fatCountt = itemView.findViewById(R.id.textView7);
            proteinCount = itemView.findViewById(R.id.txtProteinCount);
            proteintCountt = itemView.findViewById(R.id.textView6);
            idText = itemView.findViewById(R.id.txtRecipeID);
            serving = itemView.findViewById(R.id.txtServingSizeRecipe);
            checkBox = itemView.findViewById(R.id.checkBox2);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
