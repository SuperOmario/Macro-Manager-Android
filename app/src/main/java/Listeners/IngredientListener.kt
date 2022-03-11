package Listeners

import Models.Ingredient

interface IngredientListener {
    fun onServingChange(hashMap: HashMap<Int, Float>, ingredients : ArrayList<Ingredient>) {

    }
}