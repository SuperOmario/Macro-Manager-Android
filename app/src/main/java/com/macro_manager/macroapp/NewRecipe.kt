package com.macro_manager.macroapp

import Listeners.IngredientListener
import Models.Ingredient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class NewRecipe : AppCompatActivity(), IngredientListener {

    private lateinit var amounts: ArrayList<Ingredient>
    lateinit var recyclerView: RecyclerView
    private lateinit var btnProceed : FloatingActionButton
    lateinit var txtTitle : TextView
    lateinit var txtServingSize : TextView
    private var ingredients = java.util.HashMap<Int, Float>()
    lateinit var  spinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secondary_list)
        spinner = findViewById<Spinner>(R.id.spinner)
        spinner.visibility = View.INVISIBLE


        txtTitle = findViewById(R.id.txtTitle)
        txtServingSize = findViewById(R.id.txtServingSize)
        txtServingSize.isFocusable = false
        txtServingSize.isFocusableInTouchMode =false

        var recipeString : String = ""
        var foodIDs : ArrayList<Int> = ArrayList()
        val queue = Volley.newRequestQueue(this)

        recyclerView = findViewById(R.id.recyclerViewLunch)
        btnProceed = findViewById(R.id.fabProceed2)
        btnProceed.setOnClickListener { try { if (txtTitle.text.toString() != "" || !ingredients.isNullOrEmpty()) { createRecipe(ingredients) } }
        catch (e : UninitializedPropertyAccessException){ Toast.makeText(this,"Please fill out the page", Toast.LENGTH_SHORT).show()}}
        if (intent.hasExtra("Recipe")) {
            foodIDs = intent.getIntegerArrayListExtra("IDs") as ArrayList<Int>
        }

        val stringRequest = Requests.Food.getListedFoods(recyclerView, this, foodIDs)
        queue.add(stringRequest)
    }

    private fun createRecipe(hashMap: HashMap<Int, Float>) {
        val jsonBody = JSONObject()
        val jsonArray = JSONArray()
        for ((k, v) in hashMap)  {
            val ingredient = JSONObject()
            println("Key: $k Value: $v")
            ingredient.put("ingredientID", k)
            ingredient.put("servings", v)
            jsonArray.put(ingredient)
        }
        try {
            jsonBody.put("title", txtTitle.text.toString())
            jsonBody.put("servingSize", txtServingSize.text.toString().toFloat())
            jsonBody.put("ingredients", jsonArray)
            val mRequestBody = jsonBody.toString()
            println(mRequestBody)

            val queue = Volley.newRequestQueue(applicationContext)
            val stringRequest = Requests.Recipe.createRequest(mRequestBody)

            queue.add(stringRequest)
        } catch (e : JSONException) {
            e.printStackTrace()
        }
    }



    override fun onServingChange(hashMap: HashMap<Int, Float>, amounts:ArrayList<Ingredient>) {
        super.onServingChange(hashMap, amounts)
        this.ingredients = hashMap
        this.amounts = amounts



        var newInt = 0
        for (value in amounts) {
            newInt += value.Amounts.toInt()
        }
        txtServingSize.text = newInt.toString()
    }
}