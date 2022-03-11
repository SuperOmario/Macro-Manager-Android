package com.macro_manager.macroapp

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.text.DecimalFormat

class DiaryEntries : AppCompatActivity() {

    lateinit var txtTotal : TextView
    lateinit var txtCarbs : TextView
    lateinit var txtFats : TextView
    lateinit var txtProtein : TextView

    lateinit var breakfastView : RecyclerView
    lateinit var lunchView : RecyclerView
    lateinit var dinnerView : RecyclerView
    lateinit var mSnackView : RecyclerView
    lateinit var aSnackView : RecyclerView
    lateinit var eSnackView : RecyclerView

    lateinit var list : List<RecyclerView>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary_entries)
        title = intent.getStringExtra("Date")
        txtTotal = findViewById(R.id.txtTotal)
        txtCarbs = findViewById(R.id.txtCarbs)
        txtFats = findViewById(R.id.txtFats)
        txtProtein  = findViewById(R.id.txtProtein)
        txtTotal.text = "Calories: " + intent.getStringExtra("Calories")
        txtCarbs.text = "Carbohydrates: " + intent.getStringExtra("Carbohydrate")
        txtFats.text = "Fats: " + intent.getStringExtra("Fat")
        txtProtein.text = "Protein " + intent.getStringExtra("Protein")

        breakfastView = findViewById(R.id.recyclerViewBreakfast)
        lunchView = findViewById(R.id.recyclerViewLunch)
        dinnerView = findViewById(R.id.recyclerViewDinner)
        mSnackView = findViewById(R.id.recyclerViewMornSnack)
        aSnackView = findViewById(R.id.recyclerViewAftSnack)
        eSnackView = findViewById(R.id.recyclerViewESnack)

        list = listOf(breakfastView, lunchView, dinnerView, mSnackView, aSnackView, eSnackView)

        val queue : RequestQueue = Volley.newRequestQueue(this)
        val stringRequest : StringRequest = Requests.Diary.getDiariesByDate(list, intent.getStringExtra("Date").toString())
        queue.add(stringRequest)

        val donutView : DonutProgressView = findViewById(R.id.donut_view)

        donutView.cap = 1f
        donutView.submitData(getMacroPercentages())


    }

    @SuppressLint("SetTextI18n")
    fun getMacroPercentages(): List<DonutSection> {
        val cals: Float = (intent.getStringExtra("Calories")!!.toFloat())
        var carbs: Float = (intent.getStringExtra("Carbohydrate")!!.toFloat())
        var fats: Float = (intent.getStringExtra("Fat")!!.toFloat())
        var protein: Float = (intent.getStringExtra("Protein")!!.toFloat())

        carbs *= 4
        fats *= 9
        protein *= 4


        val carbsPCT = (carbs / cals)
        val fatsPCT = (fats / cals)
        val proteinPCT = (protein / cals)

        println("$carbs, $fats, $protein")
        println("$carbsPCT, $fatsPCT, $proteinPCT")

        val df: DecimalFormat = DecimalFormat("#")

        txtCarbs.text = "${txtCarbs.text} (${df.format(carbsPCT * 100)}%)"
        txtFats.text = "${txtFats.text} (${df.format(fatsPCT * 100)}%)"
        txtProtein.text = "${txtProtein.text} (${df.format(proteinPCT * 100)}%)"

//      https://github.com/futuredapp/donut
        val proteinSection = DonutSection("Protein", Color.parseColor("#FB1D32"), proteinPCT)

        val carbSection = DonutSection("Carbohydrate", Color.parseColor("#49be25"), carbsPCT)

        val fatSection = DonutSection("Fat", Color.parseColor("#25a5be"), fatsPCT)

        return listOf<DonutSection>(proteinSection, carbSection, fatSection)
    }
}
