package com.macro_manager.macroapp

import Models.Ingredient

interface IngredientListener {
    fun onServingChange(hashMap: HashMap<Int, Float>, ingredients : ArrayList<Ingredient>) {

    }
}