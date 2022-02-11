package com.macro_manager.macroapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Recipe : AppCompatActivity() {

    private var recipeView : RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe)

        val extras = intent.extras
        var recipeEntries = extras?.getString("RecipeEntries")

        recipeView = findViewById(R.id.recipeRecyclerView)

        recipeView?.adapter = RecipeAdapter(this, recipeEntries)
        recipeView?.layoutManager = LinearLayoutManager(this)

    }
}