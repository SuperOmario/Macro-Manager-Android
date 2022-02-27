package com.macro_manager.macroapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Recipe : AppCompatActivity() {

    private var recipeView : RecyclerView? = null
    lateinit var btnAddRecipe : FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe)

        btnAddRecipe = findViewById(R.id.floatingActionButtonRecipe)
        btnAddRecipe.setOnClickListener{

        }

        val queue = Volley.newRequestQueue(this)
        val url = URLs().recipeURL
        val stringRequest =  StringRequest( Request.Method.GET, url, { response ->
            Log.i("Response", response.toString())
            adapter(response)},
            { Log.e("Error", "Error retrieving response") })

        queue.add(stringRequest)

    }

    private fun adapter(recipeEntries : String) {
        recipeView = findViewById(R.id.recipeRecyclerView)

        recipeView?.adapter = RecipeAdapter(this, recipeEntries)
        recipeView?.layoutManager = LinearLayoutManager(this)
    }
}