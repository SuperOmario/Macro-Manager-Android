package Adapters;

//This code is adapted from https://www.youtube.com/watch?v=18VcnYN5_LM&ab_channel=Stevdza-San

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import Listeners.IngredientListener;
import com.macro_manager.macroapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import Models.Ingredient;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{

    JSONArray response;
    Context context;
    IngredientListener ingredientListener;
    Spinner spinner;

    HashMap<Integer, Float> ingredients = new HashMap<>();
    ArrayList<Ingredient> ingredients2 = new ArrayList<>();

    public IngredientAdapter(Context ct, JSONArray response, IngredientListener ingredientListener) {
        try {
            System.out.println(response);
            this.response = response;
            System.out.println(this.response.getJSONObject(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        context = ct;
        this.ingredientListener = ingredientListener;
        System.out.println(response);
    }

    @SuppressLint("SetTextI18n")
    private void base(IngredientViewHolder holder, int position) {
        try {
            if (response.getJSONObject(position).has("Title")) {
                holder.titleText.setText(response.getJSONObject(position).getString("Title"));
                holder.idText.setText(String.valueOf(response.getJSONObject(position).getInt("IngredientID")));
                holder.ingredientIDText.setText(String.valueOf(response.getJSONObject(position).getInt("RecipeIngredientID")));
                String servingSize = response.getJSONObject(position).getString("ServingSize");
                if (!servingSize.equals("0")) holder.serving.setText("Serving size: " + servingSize);
                else {
                    holder.serving.setText("Serving size: 100" );
                }
                DecimalFormat df = new DecimalFormat("0.00");
                holder.servings.setText("Servings: " + response.getJSONObject(position).getString("Servings"));
                holder.amount.setText(df.format (Double.parseDouble(holder.serving.getText().toString().replace("Serving size: ", "")) * Double.parseDouble(holder.servings.getText().toString().replace("Servings: ", ""))));

                Ingredient ingredient = new Ingredient(response.getJSONObject(position).getInt("RecipeIngredientID"), response.getJSONObject(position).getInt("IngredientID"), Float.parseFloat(holder.servings.getText().toString().replace("Servings: ", "")), Double.parseDouble(holder.amount.getText().toString()));

                ingredients2.add(ingredient);
                ingredients.put(response.getJSONObject(position).getInt("IngredientID"),  Float.parseFloat(holder.servings.getText().toString().replace("Servings: ", "")));
            } else {
                holder.titleText.setText(response.getJSONObject(position).getString("product_name"));
                holder.idText.setText(String.valueOf(response.getJSONObject(position).getInt("IngredientID")));
                String servingSize = response.getJSONObject(position).getString("serving_quantity");
                if (!servingSize.equals("0")) {
                    holder.serving.setText("Serving size: " + servingSize);
                } else {
                    holder.serving.setText("Serving size: 100" );
                }
                ingredients.put(response.getJSONObject(position).getInt("IngredientID"), 0.00F);
                ingredients2.add(new Ingredient(0, 0, 0, 0));
            }




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
                        ingredients.put(response.getJSONObject(position).getInt("IngredientID"), Float.parseFloat( holder.servings.getText().toString().replace("Servings: ", "")));
                        if (response.getJSONObject(position).has("RecipeIngredientID")) {
                            Ingredient ingredient = new Ingredient(response.getJSONObject(position).getInt("RecipeIngredientID"), response.getJSONObject(position).getInt("IngredientID"), Float.parseFloat(holder.servings.getText().toString().replace("Servings: ", "")), Double.parseDouble(holder.amount.getText().toString()) );
                            try {
                                ingredients2.set(position, ingredient);
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Ingredient ingredient = new Ingredient(0, response.getJSONObject(position).getInt("IngredientID"), Float.parseFloat(holder.servings.getText().toString().replace("Servings: ", "")), Double.parseDouble(holder.amount.getText().toString()));

                            ingredients2.set(position, ingredient);
                        }
                        ingredientListener.onServingChange(ingredients, ingredients2);
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
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_row,parent,false);
        return new IngredientAdapter.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        if (response != null) {
            base(holder, position);
        } else {
            holder.mainLayout.setVisibility((View.INVISIBLE));
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

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, idText, ingredientIDText, serving, servings, amount;
        ConstraintLayout mainLayout;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.txtTitle);
            idText = itemView.findViewById(R.id.txtRecipeID);
            ingredientIDText = itemView.findViewById(R.id.txtRecipeIngredientID);
            serving = itemView.findViewById(R.id.txtServingSizeRecipe);
            servings = itemView.findViewById(R.id.txtServings);
            amount = itemView.findViewById(R.id.txtAmount);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
