package com.macro_manager.macroapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Listeners.IngredientListener;
import Models.Ingredient;
import Requests.Recipe;

public class RecipeDetails extends AppCompatActivity implements IngredientListener {

    TextView title, calories, carbs, fat, protein, servingSize;

    String titlet, caloriest, carbst, fatt, proteint, servingSizet;

    Button delete, updateDetails, updateIngredients;

    RecyclerView recyclerView;
    private HashMap<Integer, Float> ingredients;
    private ArrayList<Ingredient> ingredients2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        RequestQueue queue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recyclerViewLunch);
        StringRequest stringRequest = Recipe.Companion.getIngredientsRequest(recyclerView, getIntent().getStringExtra("id"), this);
        queue.add(stringRequest);

        title = findViewById(R.id.title);
        calories = findViewById(R.id.calorieNumber);
        carbs = findViewById(R.id.carbsNumber);
        fat = findViewById(R.id.fatNumber);
        protein = findViewById(R.id.proteinNumber);
        servingSize = findViewById(R.id.servingSizeNumber);

        delete = findViewById(R.id.btnDelete);
        updateDetails = findViewById(R.id.btnUpdate);
        updateIngredients = findViewById(R.id.btnUpdateIngredients);

        title.setFocusable(true);
        title.setFocusableInTouchMode(true);

        calories.setFocusable(false);
        carbs.setFocusable(false);
        fat.setFocusable(false);
        protein.setFocusable(false);
        calories.setFocusableInTouchMode(false);
        carbs.setFocusableInTouchMode(false);
        fat.setFocusableInTouchMode(false);
        protein.setFocusableInTouchMode(false);
        servingSize.setFocusable(false);
        servingSize.setFocusableInTouchMode(false);
        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecipeDetails(v);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecipe();
            }
        });
        updateIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIngredients();
            }
        });

            getData();
            setData();
        }

    private void getData() {
        if (getIntent().hasExtra("title")) {
            titlet = getIntent().getStringExtra("title");
            caloriest = getIntent().getStringExtra("calories");
            carbst = getIntent().getStringExtra("carbs");
            fatt = getIntent().getStringExtra("fat");
            proteint = getIntent().getStringExtra("protein");
            servingSizet = getIntent().getStringExtra("servingSize");
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        title.setText(titlet);
        calories.setText(caloriest);
        carbs.setText(carbst);
        fat.setText(fatt);
        protein.setText(proteint);
        servingSize.setText(servingSizet);
    }

    public void deleteRecipe() {
        if (getIntent().hasExtra("id")) {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = Recipe.Companion.deleteRequest(getIntent().getStringExtra("id"));

            queue.add(stringRequest);
        }
    }

    public void updateRecipeDetails(View view) {
        if (getIntent().hasExtra("id")) {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("title", title.getText().toString());
                jsonObject.put("servingSize", Double.parseDouble(servingSize.getText().toString()));
            } catch(Exception e) {
                e.printStackTrace();
            }
//            for (Map.Entry<Integer, Float> entry: ingredients.entrySet()) {
//                Integer key = entry.getKey();
//                Float value = entry.getValue();
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("ingredientId", key);
//                    jsonObject.put("servings", value);
//                    jsonArray.put(jsonObject);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            String mRequestBody = jsonObject.toString();
            StringRequest stringRequest = Recipe.Companion.updateRequest(getIntent().getStringExtra("id"), mRequestBody);

            queue.add(stringRequest);
        }
    }

    public void updateIngredients() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONArray jsonArray = new JSONArray();
        try {

            for (int i = 0; i < ingredients2.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("RecipeIngredientID", ingredients2.get(i).RecipeIngredientID);
                jsonObject.put("Servings", ingredients2.get(i).Servings);
                jsonArray.put(jsonObject);
            }
//            for (Map.Entry<Integer, Double> entry : amounts.entrySet()) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("RecipeIngredientID", entry.getKey());
//                jsonObject.put("Servings", entry.getValue());
//                jsonArray.put(jsonObject);
//            }
//            Iterator<Map.Entry<Integer, Float>> ingredientsIterator = ingredients.entrySet().iterator();
//            Iterator<Map.Entry<Integer, Double>> amountsIterator = amounts.entrySet().iterator();
//            while (ingredientsIterator.hasNext() || amountsIterator.hasNext()) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("RecipeIngredientID", amountsIterator.next().getKey());
//                jsonObject.put("Servings", ingredientsIterator.next().getValue());
//                jsonArray.put(jsonObject);
//            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        String mRequestBody = jsonArray.toString();
        System.out.println(mRequestBody);
        StringRequest stringRequest = Recipe.Companion.updateIngredientsRequest(mRequestBody);

        queue.add(stringRequest);
    }

//    public void addIngredient(View view) {
//        if (getIntent().hasExtra("id")) {
//            RequestQueue queue = Volley.newRequestQueue(this);
//            String url = new URLs().getFoodURL();
//            url += "/" + getIntent().getStringExtra("id");
//            StringRequest stringRequest = new StringRequest( Request.Method.DELETE, url, response -> {
//                Intent i = new Intent(getApplicationContext(), Food.class);
//                i.putExtra("id", response);
//                Log.i("Response", response);
//                startActivity(i);
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("Error", String.valueOf(error));
//                }
//            });
//
//            queue.add(stringRequest);
//        };
//    }
    @Override
    public void onServingChange(HashMap<Integer, Float> hashMap, ArrayList<Ingredient> ingredients) {
//        IngredientListener.super.onServingChange(hashMap);
        this.ingredients = hashMap;
        this.ingredients2 = ingredients;
    }

}


