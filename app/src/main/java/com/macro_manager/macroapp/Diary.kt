package com.macro_manager.macroapp

import android.app.DatePickerDialog.*
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class Diary : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var btnProceed : FloatingActionButton
    lateinit var btnAdd : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_list)
        btnProceed = findViewById(R.id.fabProceed)
        btnProceed.visibility = View.INVISIBLE

        btnAdd = findViewById(R.id.floatingActionButton)
        btnAdd.setOnClickListener { addDiaryEntry() }

        val queue = Volley.newRequestQueue(this)
        recyclerView = findViewById(R.id.recyclerViewLunch)
        val stringRequest = Requests.Diary.getDiaries(recyclerView)

        queue.add(stringRequest)
    }

    private fun addDiaryEntry() {
        val i = Intent(this, Recipe::class.java)
        i.putExtra("Diary", "New")
        this.startActivity(i)
    }


}