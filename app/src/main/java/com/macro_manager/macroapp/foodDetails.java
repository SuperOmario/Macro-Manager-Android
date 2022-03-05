package com.macro_manager.macroapp;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class foodDetails extends AppCompatActivity {

    TextView title, calories, carbs, fat, protein, servingSize;

    String titlet, caloriest, carbst, fatt, proteint, servingSizet;

    Button delete, add, update;


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
        add = findViewById(R.id.btnAdd);
        update = findViewById(R.id.btnUpdate);

        title.setFocusable(true);
        title.setFocusableInTouchMode(true);


        if (!getIntent().hasExtra("Custom Food")) {
            if (getIntent().hasExtra("recipe")) {
                calories.setFocusable(false);
                carbs.setFocusable(false);
                fat.setFocusable(false);
                protein.setFocusable(false);
                calories.setFocusableInTouchMode(false);
                carbs.setFocusableInTouchMode(false);
                fat.setFocusableInTouchMode(false);
                protein.setFocusableInTouchMode(false);
                update.setVisibility(View.INVISIBLE);
                add.setVisibility(View.INVISIBLE);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteRecipe(v);
                    }
                });
            } else  {
//                if (getIntent().getStringExtra("barcode").equals("true"))
                calories.setFocusable(false);
                carbs.setFocusable(false);
                fat.setFocusable(false);
                protein.setFocusable(false);
                calories.setFocusableInTouchMode(false);
                carbs.setFocusableInTouchMode(false);
                fat.setFocusableInTouchMode(false);
                protein.setFocusableInTouchMode(false);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(v);
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

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       addIngredientDialog();
                    }
                });
            }
            getData();
            setData();
        } else {
            delete.setVisibility(View.INVISIBLE);
            update.setVisibility(View.INVISIBLE);
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

    public void delete(View view) {
        if (getIntent().hasExtra("id")) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = new URLs().getFoodURL();
            url += "/" + getIntent().getStringExtra("id");
            StringRequest stringRequest = new StringRequest( Request.Method.DELETE, url, response -> {
                Intent i = new Intent(getApplicationContext(), Food.class);
                i.putExtra("id", response);
                Log.i("Response", response);
                startActivity(i);
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", String.valueOf(error));
                }
            });

            queue.add(stringRequest);
        };
    }

    public void deleteRecipe(View view) {
        if (getIntent().hasExtra("id")) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = new URLs().getRecipeURL();
            url += "/" + getIntent().getStringExtra("id");
            StringRequest stringRequest = new StringRequest( Request.Method.DELETE, url, response -> {
                Intent i = new Intent(getApplicationContext(), Recipe.class);
                i.putExtra("id", response);
                Log.i("Response", response);
                startActivity(i);
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", String.valueOf(error));
                }
            });

            queue.add(stringRequest);
        }
    }

    public void add(View view) {
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
    }

    public void addCustom() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("userId", 1);
            jsonBody.put("title", title.getText());
            jsonBody.put("calories", Integer.parseInt(calories.getText().toString()));
            jsonBody.put("fat", Integer.parseInt(fat.getText().toString()));
            jsonBody.put("carbohydrate", Integer.parseInt(carbs.getText().toString()));
            jsonBody.put("protein", Integer.parseInt(protein.getText().toString()));
            jsonBody.put("serving_size", Integer.parseInt(servingSize.getText().toString()));
            final String mRequestBody = jsonBody.toString();

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = new URLs().getFoodURL();
            StringRequest stringRequest = new StringRequest( Request.Method.POST, url, response -> {
                Intent i = new Intent(getApplicationContext(), Food.class);
                i.putExtra("id", response);
                Log.i("Response", response);
                startActivity(i);
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", String.valueOf(error));
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };


            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addIngredientDialog() {
        AlertDialog.Builder addIngredientDialog = new AlertDialog.Builder(getApplicationContext());
        addIngredientDialog.setTitle("Add Ingredient to Recipe");
        addIngredientDialog.setMessage("Add to existing or new recipe?");
        addIngredientDialog.setPositiveButton("New Recipe", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        addIngredientDialog.setNegativeButton("Existing Recipe", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        addIngredientDialog.create().show();
    }

    //adapted from https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str
    public void update(View view) throws JSONException {
        if (getIntent().hasExtra("id")) {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("title", title.getText());
            jsonBody.put("calories", Integer.parseInt(calories.getText().toString()));
            jsonBody.put("fat", Integer.parseInt(fat.getText().toString()));
            jsonBody.put("carbohydrate", Integer.parseInt(carbs.getText().toString()));
            jsonBody.put("protein", Integer.parseInt(protein.getText().toString()));
            jsonBody.put("servingsize", Integer.parseInt(servingSize.getText().toString()));
            final String mRequestBody = jsonBody.toString();
            Log.i("Body", mRequestBody);

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = new URLs().getFoodURL();
            url += "/" + getIntent().getStringExtra("id");
            StringRequest stringRequest = new StringRequest( Request.Method.PATCH, url, response -> {
                Intent i = new Intent(getApplicationContext(), Food.class);
                i.putExtra("id", response);
                Log.i("Response", response);
                startActivity(i);
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", String.valueOf(error));
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            queue.add(stringRequest);
        };
    }
}