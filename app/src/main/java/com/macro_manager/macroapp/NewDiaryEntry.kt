package com.macro_manager.macroapp

import Fragments.DatePickerFragment
import Listeners.DiaryRecipeListener
import Requests.Recipe
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NewDiaryEntry : AppCompatActivity(), DatePickerDialog.OnDateSetListener, DiaryRecipeListener {

    lateinit var recyclerView: RecyclerView
    private lateinit var btnProceed : FloatingActionButton
    lateinit var txtTitle : TextView
    private lateinit var txtServingSize : TextView
    private lateinit var txtServingSizet : TextView
    private lateinit var txtServingmeasurement : TextView
    lateinit var spinner : Spinner
    lateinit var arrayList: HashMap<Int, Float>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secondary_list)
        txtServingSize = findViewById(R.id.txtServingSize)
        txtServingSize.visibility = View.INVISIBLE
        txtServingSizet = findViewById(R.id.textView2)
        txtServingSizet.text = "Meal Time"
        txtServingmeasurement = findViewById(R.id.textView3)
        txtServingmeasurement.visibility = View.INVISIBLE
        spinner = findViewById(R.id.spinner)
        spinner.visibility = View.VISIBLE
        var recipeIDs : ArrayList<Int> = ArrayList()
        recyclerView = findViewById(R.id.recyclerViewLunch)
        txtTitle = findViewById(R.id.txtTitle)
        val queue : RequestQueue = Volley.newRequestQueue(this)
        if (intent.hasExtra("Diary")) {
            println("Getting to set recipeIDs as intent variable")
            recipeIDs = intent.getIntegerArrayListExtra("IDs") as ArrayList<Int>
        }
        val arrayRequest : JsonArrayRequest = Recipe.getListedRecipes(recyclerView,  this, recipeIDs)
        queue.add(arrayRequest)
        datePicker()

        btnProceed = findViewById(R.id.fabProceed2)
        btnProceed.setOnClickListener { try { if (txtTitle.text.toString() != "" || !arrayList.isNullOrEmpty()) createDiaryEntries()
        else Toast.makeText(this,"Please fill out the page", Toast.LENGTH_SHORT).show()
        } catch (e : UninitializedPropertyAccessException){ Toast.makeText(this,"Please fill out the page", Toast.LENGTH_SHORT).show()}}

    }

    private fun datePicker(){
        var datePicker : DialogFragment = DatePickerFragment()
        datePicker.show(supportFragmentManager, "Date Picker")
    }
    // adapted from https://www.youtube.com/watch?v=33BFCdL0Di0&ab_channel=CodinginFlow
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var c : Calendar = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONDAY, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        var initialDateString : String = DateFormat.getDateInstance().format(c.time)
        println(initialDateString)
        var day : String
        var month : String
        var year : String
        if (initialDateString.length == 10) {
            day  = initialDateString.slice(IntRange(0,0))
            month = initialDateString.slice(IntRange(2,4))
            when (month) {
                "Jan" -> month = "01"
                "Feb" -> month = "02"
                "Mar" -> month = "03"
                "Apr" -> month = "04"
                "May" -> month = "05"
                "Jun" -> month = "06"
                "Jul" -> month = "07"
                "Aug" -> month = "08"
                "Sep" -> month = "09"
                "Oct" -> month = "10"
                "Nov" -> month = "11"
                "Dec" -> month = "12"
            }
            year  = initialDateString.slice(IntRange(6,9))
        } else {
            day = initialDateString.slice(IntRange(0, 1))
            month = initialDateString.slice(IntRange(3, 5))
            when (month) {
                "Jan" -> month = "01"
                "Feb" -> month = "02"
                "Mar" -> month = "03"
                "Apr" -> month = "04"
                "May" -> month = "05"
                "Jun" -> month = "06"
                "Jul" -> month = "07"
                "Aug" -> month = "08"
                "Sep" -> month = "09"
                "Oct" -> month = "10"
                "Nov" -> month = "11"
                "Dec" -> month = "12"
            }
            year = initialDateString.slice(IntRange(7, 10))
        }
        var finalDateString = "$year/$month/$day"
        txtTitle.text = finalDateString
    }

    fun createDiaryEntries() {
        val jsonArray = JSONArray()
        for ((k, v) in arrayList)  {
            val entry = JSONObject()
            entry.put("recipeID", k)
            entry.put("servings", v)
            entry.put("date", txtTitle.text.toString())
            entry.put("meal", spinner.selectedItem.toString())
            jsonArray.put(entry)
        }
        try {
            val mRequestBody = jsonArray.toString()
            println(mRequestBody)

            val queue = Volley.newRequestQueue(applicationContext)
            val stringRequest = Requests.Diary.createDiaries(mRequestBody)

            queue.add(stringRequest)
        } catch (e : JSONException) {
            e.printStackTrace()
        }
    }


    override fun onRecipeChange(hashMap: HashMap<Int, Float>) {
        super.onRecipeChange(hashMap)
        this.arrayList = hashMap
    }
}