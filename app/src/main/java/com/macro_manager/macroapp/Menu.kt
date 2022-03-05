package com.macro_manager.macroapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanIntentResult.parseActivityResult
import org.json.JSONObject

// taken from https://www.tutorialspoint.com/barcode-scanning-in-android-using-kotlin
//allows barcode scanning
class Menu : AppCompatActivity()  {
    lateinit var btnBarcode: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
        title = "MacroManager"
        btnBarcode = findViewById<Button>(R.id.btnScan)
        btnBarcode.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this@Menu)
            intentIntegrator.setCaptureActivity(AnyOrientationCaptureActivity::class.java)
            intentIntegrator.setOrientationLocked(false)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setBarcodeImageEnabled(false)
            intentIntegrator.initiateScan()
        }
    }
//  adapted from https://developer.android.com/training/volley/simple
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result = parseActivityResult(resultCode, data)
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

    fun diary(view: View) {
        val queue = Volley.newRequestQueue(this)
        val url = URLs().diaryURL
        val stringRequest =  StringRequest( Request.Method.GET, url, { response ->
            val i = Intent(applicationContext, Diary::class.java)
            i.putExtra("DiaryEntries", response)
            Log.i("Response", response.toString())
            startActivity(i)},
            { Log.e("Error", "Error retrieving response") })

        queue.add(stringRequest)

    }

    fun recipe(view: View) {
//        val queue = Volley.newRequestQueue(this)
//        val url = URLs().recipeURL
//        val stringRequest =  StringRequest( Request.Method.GET, url, { response ->
//            val i = Intent(applicationContext, Recipe::class.java)
//            i.putExtra("RecipeEntries", response)
//            Log.i("Response", response.toString())
//            startActivity(i)},
//            { Log.e("Error", "Error retrieving response") })
//
//        queue.add(stringRequest)
        val i = Intent(applicationContext, Recipe::class.java)
        startActivity(i)
    }

    fun food(view: View) {
//        val queue = Volley.newRequestQueue(this)
//        val url = URLs().foodURL
//        val stringRequest =  StringRequest( Request.Method.GET, url, { response ->
//            val i = Intent(applicationContext, Food::class.java)
//            i.putExtra("FoodEntries", response)
//            Log.i("Response", response.toString())
//            startActivity(i)},
//            { Log.e("Error", "Error retrieving response") })
//
//        queue.add(stringRequest)
        val i = Intent(applicationContext, Food::class.java)
        startActivity(i)
    }

    fun menu(view: View) {
        val i = Intent(applicationContext, MenuScanner::class.java)
        startActivity(i)
    }

}