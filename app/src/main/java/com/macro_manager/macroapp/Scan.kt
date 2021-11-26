package com.macro_manager.macroapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanIntentResult.parseActivityResult

// taken from https://www.tutorialspoint.com/barcode-scanning-in-android-using-kotlin
//allows barcode scanning
class Scan : AppCompatActivity()  {
    lateinit var btnBarcode: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan)
        title = "MacroManager"
        btnBarcode = findViewById<Button>(R.id.button)
        btnBarcode.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this@Scan)
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
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun scan (result : String) {
        println(result)
        val queue = Volley.newRequestQueue(this)
        val url = "https://macro-manager-dev.herokuapp.com/food/$result"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                //Display the first 500 characters of the response string.
                //response.substring was causing crashes, must figure out why
                Log.d("Response", "Response is: $response")
                Toast.makeText(this, response, Toast.LENGTH_LONG).show()
            },
            { error ->  Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show() })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

}