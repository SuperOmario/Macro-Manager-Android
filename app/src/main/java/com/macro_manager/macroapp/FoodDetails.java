package com.macro_manager.macroapp;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import Requests.Recipe;

public class FoodDetails extends AppCompatActivity {

    TextView title, calories, carbs, fat, protein, servingSize;

    String titlet, caloriest, carbst, fatt, proteint, servingSizet;

    Button delete, add, update;

    Float calsNum=0F, carbsNum=0F, fatsNum=0F, proteinsNum = 0F;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);



        title = findViewById(R.id.title);
        calories = findViewById(R.id.calorieNumber);
        carbs = findViewById(R.id.carbsNumber);
        fat = findViewById(R.id.fatNumber);
        protein = findViewById(R.id.proteinNumber);
        servingSize = findViewById(R.id.servingSizeNumber);

        delete = findViewById(R.id.btnDelete);
        update = findViewById(R.id.btnUpdate);
        add = findViewById(R.id.btnAdd);
        add.setVisibility(View.INVISIBLE);

        title.setFocusable(true);
        title.setFocusableInTouchMode(true);


        if (!getIntent().hasExtra("Custom Food")) {
                if (getIntent().getStringExtra("barcode").equals("true")) {
                    calories.setFocusable(false);
                    carbs.setFocusable(false);
                    fat.setFocusable(false);
                    protein.setFocusable(false);
                    calories.setFocusableInTouchMode(false);
                    carbs.setFocusableInTouchMode(false);
                    fat.setFocusableInTouchMode(false);
                    protein.setFocusableInTouchMode(false);
                } else {
                    calories.setFocusable(false);
                    carbs.setFocusable(true);
                    carbs.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            validateCalsCarbs();
                        }
                    });
                    fat.setFocusable(true);
                    fat.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            validateCalsFats();
                        }
                    });
                    protein.setFocusable(true);
                    protein.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            validateCalsProtein();
                        }
                    });
                    calories.setFocusableInTouchMode(false);
                    carbs.setFocusableInTouchMode(true);
                    fat.setFocusableInTouchMode(true);
                    protein.setFocusableInTouchMode(true);
                }
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteFood(v);
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        update(v);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            getData();
            setData();
        } else {
            calories.setFocusable(false);
            calories.setFocusableInTouchMode(false);
            carbs.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    validateCalsCarbs();
                }
            });
            protein.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    validateCalsProtein();
                }
            });
            fat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    validateCalsFats();
                }
            });
            delete.setVisibility(View.INVISIBLE);
            update.setVisibility(View.INVISIBLE);
            add.setVisibility(View.VISIBLE);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCustom();
                }
            });
        }
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

    public void deleteFood(View view) {
        if (getIntent().hasExtra("id")) {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = Requests.Food.Companion.deleteFood(getIntent().getStringExtra("id"));

            queue.add(stringRequest);
        };
    }

    public void validateCalsProtein() {
        proteinsNum = Float.parseFloat(protein.getText().toString());
        calsNum = (fatsNum * 9) + (carbsNum * 4) + (proteinsNum * 4);
        calories.setText(calsNum.toString());
    }

    public void validateCalsCarbs() {
        carbsNum = Float.parseFloat(carbs.getText().toString());
        calsNum = (fatsNum * 9) + (carbsNum * 4) + (proteinsNum * 4);
        calories.setText(calsNum.toString());
    }
    public void validateCalsFats() {
        fatsNum = Float.parseFloat(fat.getText().toString());
        calsNum = (fatsNum * 9) + (carbsNum * 4) + (proteinsNum * 4);
        calories.setText(calsNum.toString());
    }



    public void addCustom() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("userId", 1);
            jsonBody.put("title", title.getText());
            jsonBody.put("calories", Float.parseFloat(calories.getText().toString()));
            jsonBody.put("fat", Float.parseFloat(fat.getText().toString()));
            jsonBody.put("carbohydrate", Float.parseFloat(carbs.getText().toString()));
            jsonBody.put("protein",Float.parseFloat(protein.getText().toString()));
            jsonBody.put("serving_size", Float.parseFloat(servingSize.getText().toString()));
            final String mRequestBody = jsonBody.toString();

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = Requests.Food.Companion.customFood(mRequestBody);

            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //adapted from https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str
    public void update(View view) throws JSONException {
        if (getIntent().hasExtra("id")) {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("title", title.getText());
            jsonBody.put("calories", Double.parseDouble(calories.getText().toString()));
            jsonBody.put("fat", Double.parseDouble(fat.getText().toString()));
            jsonBody.put("carbohydrate", Double.parseDouble(carbs.getText().toString()));
            jsonBody.put("protein", Double.parseDouble(protein.getText().toString()));
            jsonBody.put("servingsize", Double.parseDouble(servingSize.getText().toString()));
            final String mRequestBody = jsonBody.toString();
            Log.i("Body", mRequestBody);

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = Requests.Food.Companion.updateFood(mRequestBody, getIntent().getStringExtra("id"));

            queue.add(stringRequest);
        };
    }
}