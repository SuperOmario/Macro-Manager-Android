package com.macro_manager.macroapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanIntentResult

class dbView : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var floatingActionButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food)

        if (intent.hasExtra("Category")) {
            when (intent.getStringExtra("Category")){
                "Food" -> {val queue = Volley.newRequestQueue(this)
                    recyclerView = findViewById(R.id.recyclerView)
                    val stringRequest = Requests.Food.getFood(recyclerView)
                    queue.add(stringRequest)

                    floatingActionButton = findViewById(R.id.floatingActionButton)
                    floatingActionButton.setOnClickListener {
                        newFoodDialog()
                        }
                }
                "Recipe" -> {floatingActionButton = findViewById(R.id.floatingActionButton)
                    floatingActionButton.setOnClickListener{

                    }
                    recyclerView = findViewById(R.id.recyclerView)

                    val queue = Volley.newRequestQueue(this)
                    val stringRequest : StringRequest = Requests.Recipe.getRequest(recyclerView)
                    queue.add(stringRequest)}
                "Diary" -> {val queue = Volley.newRequestQueue(this)
                    recyclerView = findViewById(R.id.recyclerView)
                    val stringRequest = Requests.Diary.getDiaries(recyclerView)

                    queue.add(stringRequest)}
        }


        }
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

    fun newFoodDialog() {
        //adapted from https://www.youtube.com/watch?v=men8GB-7yM0&ab_channel=Stevdza-San
        val foodDialog : AlertDialog.Builder = AlertDialog.Builder(this)
        foodDialog.setTitle("Add New Food")
        foodDialog.setMessage("Which option would you like?")
        foodDialog.setPositiveButton("Scan Barcode") { _, _ ->
            run {
                val intentIntegrator = IntentIntegrator(this@dbView)
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
                val intent = Intent(this, Details::class.java)
                intent.putExtra("Custom Food", true)
                this.startActivity(intent)
            }
        }

        foodDialog.create().show()
    }
}