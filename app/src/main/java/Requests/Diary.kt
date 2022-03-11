package Requests

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import Adapters.DiaryAdapter
import android.content.Intent
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonArrayRequest
import com.macro_manager.macroapp.Diary
import com.macro_manager.macroapp.MainApplication
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class Diary {
    companion object {
        fun getDiaries(diaryView: RecyclerView) : StringRequest{
            val url = URLs().diaryURL
            return StringRequest( Request.Method.GET, url, { response ->
                Log.i("Response", response.toString())
                adapter(response, diaryView)},
                { Log.e("Error", "Error retrieving response") })
        }

        fun getDiariesByDate(list : List<RecyclerView>, date : String): StringRequest {
            val url = URLs().diaryURL + "/$date"
            val jsonObject = JSONObject()
            jsonObject.put("date", date)
            return StringRequest( Request.Method.GET, url, { response ->
                val jsonArray = JSONArray(response)
                val bfastArray  = JSONArray()
                val lunchArray = JSONArray()
                val dinnerArray = JSONArray()
                val mSnackArray = JSONArray()
                val aSnackArray = JSONArray()
                val eSnackArray = JSONArray()
                for (i in 0 until jsonArray.length() ) {
                    val entry : JSONObject = jsonArray.getJSONObject(i)
                    when (entry.get("Meal").toString()) {
                        "Breakfast" -> bfastArray.put(entry)
                        "Lunch" -> lunchArray.put(entry)
                        "Dinner" -> dinnerArray.put(entry)
                        "Morning Snack" -> mSnackArray.put(entry)
                        "Afternoon Snack" -> aSnackArray.put(entry)
                        "Evening Snack" -> eSnackArray.put(entry)
                    }
                }
                adapter(bfastArray.toString(), list[0])
                adapter(lunchArray.toString(), list[1])
                adapter(dinnerArray.toString(), list[2])
                adapter(mSnackArray.toString(), list[3])
                adapter(aSnackArray.toString(), list[4])
                adapter(eSnackArray.toString(), list[5]) },
                { Log.e("Error", "Error retrieving response") })
        }

        fun createDiaries(mRequestBody: String): StringRequest {
            val url = URLs().diaryURL + "/diaries"
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response: String? ->
                    val i = Intent(
                        MainApplication.applicationContext(),
                        Diary::class.java
                    )
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    Log.i("Response", response!!)
                    MainApplication.applicationContext().startActivity(i)
                },
                Response.ErrorListener { error -> Log.e("Error", error.toString()) }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray? {
                    return try {
                        if (mRequestBody == null) {
                            null
                        } else {
                            mRequestBody.toByteArray(charset("utf-8"))
                        }
                    } catch (e : Exception) {
                        VolleyLog.wtf(
                            "Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody,
                            "utf-8"
                        )
                        null
                    }
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    var responseString = ""
                    if (response != null) {
                        responseString = response.statusCode.toString()
                    }
                    return Response.success(
                        responseString,
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }
            return stringRequest
        }


        private fun adapter(diaryEntries : String, diaryView : RecyclerView) {
            val context = MainApplication.applicationContext()
            diaryView?.adapter = DiaryAdapter(context, diaryEntries)
            diaryView?.layoutManager = LinearLayoutManager(context)
        }
    }
}

