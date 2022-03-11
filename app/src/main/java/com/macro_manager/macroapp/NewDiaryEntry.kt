package com.macro_manager.macroapp

import Fragments.DatePickerFragment
import Listeners.DiaryRecipeListener
import Listeners.RecipeListener
import Requests.Recipe
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NewDiaryEntry : AppCompatActivity(), DatePickerDialog.OnDateSetListener, DiaryRecipeListener {

    lateinit var recyclerView: RecyclerView
    private lateinit var btnProceed : FloatingActionButton
    lateinit var txtTitle : TextView
    lateinit var txtServingSize : TextView
    lateinit var arrayList: HashMap<Int, Float>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secondary_list)

        var recipeIDs : ArrayList<Int> = ArrayList()
        recyclerView = findViewById(R.id.recyclerView)
        txtTitle = findViewById(R.id.txtTitle)
        val queue : RequestQueue = Volley.newRequestQueue(this)

        if (intent.hasExtra("Diary")) {
            recipeIDs = intent.getIntegerArrayListExtra("IDs") as ArrayList<Int>
        }
        val arrayRequest : JsonArrayRequest = Recipe.getListedRecipes(recyclerView,  this, recipeIDs)
        queue.add(arrayRequest)
        datePicker()
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
        var day : String = initialDateString.slice(IntRange(0,1))
        var month : String = initialDateString.slice(IntRange(3,5))
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
        var year : String = initialDateString.slice(IntRange(7,10))
        var finalDateString = "$year/$month/$day"
        txtTitle.text = finalDateString
    }

    override fun onRecipeChange(hashMap: HashMap<Int, Float>) {
        super.onRecipeChange(hashMap)
        this.arrayList = hashMap
    }
}