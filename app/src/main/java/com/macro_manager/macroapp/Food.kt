package com.macro_manager.macroapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Food : AppCompatActivity() {

    private var foodView : RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food)

        val extras = intent.extras
        var foodEntries = extras?.getString("FoodEntries")

        foodView = findViewById(R.id.foodRecyclerView)

        foodView?.adapter = FoodAdapter(this, foodEntries)
        foodView?.layoutManager = LinearLayoutManager(this)

    }
}