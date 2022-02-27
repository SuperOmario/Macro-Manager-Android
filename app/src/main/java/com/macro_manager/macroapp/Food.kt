package com.macro_manager.macroapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanIntentResult
import org.json.JSONObject

class Food : AppCompatActivity() {

    private var foodView : RecyclerView? = null
    lateinit var btnBarcode: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food)

        btnBarcode = findViewById(R.id.floatingActionButtonFood)
        btnBarcode.setOnClickListener {

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
                    val intent = Intent(this, foodDetails::class.java)
                    intent.putExtra("Custom Food", true)
                    this.startActivity(intent)
                }
            }

            foodDialog.create().show()


//            val intentIntegrator = IntentIntegrator(this@Food)
//            intentIntegrator.setCaptureActivity(AnyOrientationCaptureActivity::class.java)
//            intentIntegrator.setOrientationLocked(false)
//            intentIntegrator.setBeepEnabled(false)
//            intentIntegrator.setCameraId(0)
//            intentIntegrator.setPrompt("SCAN")
//            intentIntegrator.setBarcodeImageEnabled(false)
//            intentIntegrator.initiateScan()
        }

        val queue = Volley.newRequestQueue(this)
        val url = URLs().foodURL
        val stringRequest =  StringRequest( Request.Method.GET, url, { response ->
            Log.i("Response", response.toString())
            adapter(response)},
            { Log.e("Error", "Error retrieving response") })
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
        println(result)
        val queue = Volley.newRequestQueue(this)
        val url = URLs().foodURL + "/" + result

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.POST, url,
            { response ->
                //Display the first 500 characters of the response string.
                //response.substring was causing crashes, must figure out why
                Log.d("Response", "Response is: $response")
                val intent = Intent(this, foodDetails::class.java)
                val food = JSONObject(response)
                intent.putExtra("title", food.getString("product_name"))
                intent.putExtra("calories", food.getJSONObject("nutriments").getString("energy-kcal_100g"))
                intent.putExtra("carbs", food.getJSONObject("nutriments").getString("carbohydrates_100g"))
                intent.putExtra("fat", food.getJSONObject("nutriments").getString("fat_100g"))
                intent.putExtra("protein", food.getJSONObject("nutriments").getString("proteins_100g"))
                intent.putExtra("id", food.getString("IngredientID"))
                intent.putExtra("servingSize", food.getString("serving_quantity"))
                this.startActivity(intent)
            },
            { error ->  Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show() })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun adapter(foodEntries : String) {
        foodView = findViewById(R.id.foodRecyclerView)

        foodView?.adapter = FoodAdapter(this, foodEntries)
        foodView?.layoutManager = LinearLayoutManager(this)
    }
}