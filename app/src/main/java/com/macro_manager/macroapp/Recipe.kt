package com.macro_manager.macroapp

import Listeners.RecipeListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Recipe : AppCompatActivity(), RecipeListener {

    lateinit var recyclerView: RecyclerView
    lateinit var  floatingActionButton : FloatingActionButton
    lateinit var btnProceed : FloatingActionButton
    lateinit var recipeIds : ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_list)
        var diaryString : String = ""
        btnProceed = findViewById(R.id.fabProceed)
        btnProceed.visibility = View.INVISIBLE
        floatingActionButton = findViewById(R.id.floatingActionButton)
        if (intent.hasExtra("Diary")) {
            when (intent.getStringExtra("Diary")) {
                "New" -> {
                    btnProceed.visibility = View.VISIBLE

                    btnProceed.setOnClickListener { try {
                        if (!recipeIds.isNullOrEmpty()) btnProceed(recipeIds, "New", intent.getStringExtra("ID").toString())
                        else Toast.makeText(this,"Please select a recipe", Toast.LENGTH_SHORT).show()
                    } catch (e : UninitializedPropertyAccessException) {
                        Toast.makeText(this,"Please select a recipe", Toast.LENGTH_SHORT).show()
                    }}
                    floatingActionButton.visibility = View.INVISIBLE
                    diaryString = "New"
                }
                "Update" -> {

                }
                null -> {
                    btnProceed.visibility = View.INVISIBLE
                    diaryString = ""
                    floatingActionButton.setOnClickListener {
                        val i = Intent(this, Food::class.java)
                        i.putExtra("Recipe", "New")
                        this.startActivity(i)
                    }
                }
        }
        } else {
            floatingActionButton.setOnClickListener {
                val i = Intent(this, Food::class.java)
                i.putExtra("Recipe", "New")
                this.startActivity(i)
            }
        }
        recyclerView = findViewById(R.id.recyclerViewLunch)

        val queue = Volley.newRequestQueue(this)
        val stringRequest : StringRequest = Requests.Recipe.getRequest(recyclerView, diaryString, this)
        queue.add(stringRequest)
    }

    private fun btnProceed(recipeIds: ArrayList<Int>, diary: String, id: String) {
        val i = Intent(this, NewDiaryEntry::class.java)
        i.putExtra("Diary", diary)
        i.putExtra("IDs", recipeIds)
        if (id != "0") {
            i.putExtra("ID", id)
        }
        this.startActivity(i)
    }

    override fun onRecipeChange(arrayList: ArrayList<Int>) {
        super.onRecipeChange(arrayList)
        this.recipeIds = arrayList
    }

}
