package Adapters;

//This code is adapted from https://www.youtube.com/watch?v=18VcnYN5_LM&ab_channel=Stevdza-San

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import Listeners.DiaryRecipeListener;
import Listeners.IngredientListener;
import Models.Ingredient;

public class DiaryRecipeAdapter extends RecyclerView.Adapter<DiaryRecipeAdapter.DiaryRecipeViewHolder>{

    JSONArray response;
    Context context;
    DiaryRecipeListener diaryRecipeListener;

    HashMap<Integer, Float> recipes = new HashMap<>();

    public DiaryRecipeAdapter(Context ct, JSONArray response, DiaryRecipeListener diaryRecipeListener) {
        try {
            System.out.println(response);
            this.response = response;
            System.out.println(this.response.getJSONObject(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        context = ct;
        this.diaryRecipeListener = diaryRecipeListener;
        System.out.println(response);
    }

    @SuppressLint("SetTextI18n")
    private void base(DiaryRecipeViewHolder holder, int position) {
        try {
            if (response.getJSONObject(position).has("Title")) {
                holder.titleText.setText(response.getJSONObject(position).getString("Title"));
                holder.idText.setText(String.valueOf(response.getJSONObject(position).getInt("RecipeID")));
                String servingSize = response.getJSONObject(position).getString("ServingSize");
                if (!servingSize.equals("0")) holder.serving.setText("Serving size: " + servingSize);
                else {
                    holder.serving.setText("Serving size: 100" );
                }
                DecimalFormat df = new DecimalFormat("0.00");
//                holder.servings.setText("Servings: " + response.getJSONObject(position).getString("Servings"));
//                holder.amount.setText(df.format (Double.parseDouble(holder.serving.getText().toString().replace("Serving size: ", "")) * Double.parseDouble(holder.servings.getText().toString().replace("Servings: ", ""))));

            }
//            else {
//                holder.titleText.setText(response.getJSONObject(position).getString("product_name"));
//                holder.idText.setText(String.valueOf(response.getJSONObject(position).getInt("IngredientID")));
//                String servingSize = response.getJSONObject(position).getString("serving_quantity");
//                if (!servingSize.equals("0")) {
//                    holder.serving.setText("Serving size: " + servingSize);
//                } else {
//                    holder.serving.setText("Serving size: 100" );
//                }
//            }




            holder.amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        String string = holder.serving.getText().toString();
                        string = string.replace("Serving size: ", "");
                        double newInt = Double.parseDouble(string);
                        DecimalFormat df = new DecimalFormat("0.00");
                        holder.servings.setText(new StringBuilder().append("Servings: ").append(df.format(Double.parseDouble(holder.amount.getText().toString()) / newInt)).toString());
                        recipes.put(response.getJSONObject(position).getInt("RecipeID"), Float.parseFloat( holder.servings.getText().toString().replace("Servings: ", "")));
                        diaryRecipeListener.onRecipeChange(recipes);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public DiaryRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_row,parent,false);
        return new DiaryRecipeAdapter.DiaryRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryRecipeViewHolder holder, int position) {
        if (response != null) {
            base(holder, position);
        } else {

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

    public class DiaryRecipeViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, idText, serving, servings, amount;
        ConstraintLayout mainLayout;

        public DiaryRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.txtTitle);
            idText = itemView.findViewById(R.id.txtRecipeID);
            serving = itemView.findViewById(R.id.txtServingSizeRecipe);
            servings = itemView.findViewById(R.id.txtServings);
            amount = itemView.findViewById(R.id.txtAmount);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
