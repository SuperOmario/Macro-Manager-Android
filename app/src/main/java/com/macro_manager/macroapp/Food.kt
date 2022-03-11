package com.macro_manager.macroapp

import Listeners.FoodListener
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanIntentResult

class Food : AppCompatActivity(), FoodListener {

    lateinit var recyclerView: RecyclerView
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var btnProceed : FloatingActionButton
    private var foodIds: ArrayList<Int> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_list)
        var recipeString : String = ""
        val queue = Volley.newRequestQueue(this)
        recyclerView = findViewById(R.id.recyclerView)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        btnProceed = findViewById(R.id.fabProceed)
        if (intent.hasExtra("Recipe")) {
            when (intent.getStringExtra("Recipe")) {
                "New" -> {
                    btnProceed.visibility = View.VISIBLE;
                    btnProceed.setOnClickListener { btnProceed(this.foodIds, "New", "0") }
                    recipeString = "New"
                    floatingActionButton.visibility = View.INVISIBLE
                }
                "Update" -> {
                    btnProceed.visibility = View.VISIBLE;
                    btnProceed.setOnClickListener { btnProceed(this.foodIds, "Update", intent.getStringExtra("ID").toString()) }
                    recipeString = "Update"
                    floatingActionButton.visibility = View.INVISIBLE
                }
                null -> {
                    btnProceed.visibility = View.INVISIBLE
                    recipeString = ""
                    floatingActionButton.setOnClickListener { newFoodDialog() }
                }
            }
        } else {
            btnProceed.visibility = View.INVISIBLE
            floatingActionButton.setOnClickListener { newFoodDialog() }
        }

        val stringRequest = Requests.Food.getFood(recyclerView, recipeString, this)
        queue.add(stringRequest)

    }


    //  adapted from https://developer.android.com/training/volley/simple
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result = ScanIntentResult.parseActivityResult(resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Scan", "Scanned")
                scan(result.contents)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun scan (result : String) {
        val queue = Volley.newRequestQueue(this)


        // Request a string response from the provided URL.
        val stringRequest = Requests.Food.postBarcode(result)


// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun newFoodDialog() {
        //adapted from https://www.youtube.com/watch?v=men8GB-7yM0&ab_channel=Stevdza-San
        val foodDialog : AlertDialog.Builder = AlertDialog.Builder(this)
        foodDialog.setTitle("Add New Food")
        foodDialog.setMessage("Which option would you like?")
        foodDialog.setPositiveButton("Scan Barcode") { _, _ ->
            run {
                val intentIntegrator = IntentIntegrator(this@Food)
                intentIntegrator.setCaptureActivity(AnyOrientationCaptureActivity::class.java)
                intentIntegrator.setOrientationLocked(false)
                intentIntegrator.setBeepEnabled(false)
                intentIntegrator.setCameraId(0)
                intentIntegrator.setPrompt("SCAN")
                intentIntegrator.setBarcodeImageEnabled(false)
                intentIntegrator.initiateScan()

            }
        }
        foodDialog.setNegativeButton("Custom Food") { _, _ ->
            run {
                val intent = Intent(this, FoodDetails::class.java)
                intent.putExtra("Custom Food", true)
                this.startActivity(intent)
            }
        }

        foodDialog.create().show()
    }

    private fun btnProceed(foodIds : ArrayList<Int>, recipe: String, id: String) {
        val i = Intent(this, NewRecipe::class.java)
        i.putExtra("Recipe", recipe)
        i.putExtra("IDs", foodIds)
        if (id != "0") {
            i.putExtra("ID", id)
        }
        this.startActivity(i)
    }

//  adapted from  https://www.youtube.com/watch?v=5YFPkFaLcIo&ab_channel=MickeyFaisal
    override fun onFoodChange(arrayList: ArrayList<Int>) {
        super.onFoodChange(arrayList)
        this.foodIds = arrayList
    }
}