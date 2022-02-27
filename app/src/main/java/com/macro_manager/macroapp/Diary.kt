package com.macro_manager.macroapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray

class Diary : AppCompatActivity() {

    private var diaryView : RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary)

        val queue = Volley.newRequestQueue(this)
        val url = URLs().diaryURL
        val stringRequest =  StringRequest( Request.Method.GET, url, { response ->
            Log.i("Response", response.toString())
            adapter(response)},
            { Log.e("Error", "Error retrieving response") })

        queue.add(stringRequest)

    }

    private fun adapter(diaryEntries : String) {
        diaryView = findViewById(R.id.diaryRecyclerView)

        diaryView?.adapter = DiaryAdapter(this, diaryEntries)
        diaryView?.layoutManager = LinearLayoutManager(this)
    }

}