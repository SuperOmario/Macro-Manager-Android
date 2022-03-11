package Requests

import Adapters.FoodAdapter
import Adapters.IngredientAdapter
import Listeners.FoodListener
import Listeners.IngredientListener
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.macro_manager.macroapp.*
import com.macro_manager.macroapp.Food
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class Food {

    companion object {
        fun getFood(foodView: RecyclerView, recipe : String, foodListener: FoodListener): StringRequest{
            val url = URLs().foodURL
            return StringRequest( Request.Method.GET, url, { response ->
                Log.i("Response", response.toString())
                foodAdapter(response, foodView, recipe, foodListener)},
                { Log.e("Error", "Error retrieving response") })
        }

        fun postBarcode(result : String): StringRequest{
            val context = MainApplication.applicationContext()
            val url = URLs().foodURL + "/" + result
            return StringRequest(
                Request.Method.POST, url,
                { response ->
                    Log.d("Response", "Response is: $response")

                    val intent = Intent(context, FoodDetails::class.java)
                    val food = JSONObject(response)
                    intent.putExtra("title", food.getString("product_name"))
                    intent.putExtra("calories", food.getJSONObject("nutriments").getString("energy-kcal_100g"))
                    intent.putExtra("carbs", food.getJSONObject("nutriments").getString("carbohydrates_100g"))
                    intent.putExtra("fat", food.getJSONObject("nutriments").getString("fat_100g"))
                    intent.putExtra("protein", food.getJSONObject("nutriments").getString("proteins_100g"))
                    intent.putExtra("id", food.getString("IngredientID"))
                    intent.putExtra("servingSize", food.getString("serving_quantity"))
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                },
                { error ->  Toast.makeText(context, "Could not find food with that barcode, please enter manually", Toast.LENGTH_LONG).show() })
        }

        fun getListedFoods(ingredientView: RecyclerView, ingredientListener: IngredientListener, foodIds : ArrayList<Int>): JsonArrayRequest{
            val jsonArray = JSONArray()
            val foodArray = JSONArray()
            for (i in foodIds) {
                foodArray.put(i)
            }
            val jsonObject = JSONObject().put("IDs", foodArray)
            jsonArray.put(jsonObject)
            val url = URLs().foodURL + "/ingredients"
            val stringRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.POST, url, jsonArray,
                Response.Listener { response: JSONArray ->
                    ingredientAdapter(response, ingredientView, ingredientListener)
                    Log.i("Response", response.toString()!!)
                },
                Response.ErrorListener { error -> Log.e("Error", error.toString()) }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray? {
                    return try {
                        if (jsonArray == null) {
                            null
                        } else {
                            jsonArray.toString().toByteArray(charset("utf-8"))
                        }
                    } catch (e : Exception) {
                        VolleyLog.wtf(
                            "Unsupported Encoding while trying to get the bytes of %s using %s",
                            jsonArray,
                            "utf-8"
                        )
                        null
                    }
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<JSONArray>? {
                    var responseArray = JSONArray()
                    if (response != null) {
                        responseArray = JSONArray(response.data.decodeToString())
                    }
                    return Response.success(
                        responseArray,
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }
            return stringRequest
        }

        fun deleteFood(id : String) : StringRequest {
            var url = URLs().foodURL
            url += "/$id"
            return StringRequest(
                Request.Method.DELETE, url,
                { response: String? ->
                    val i = Intent(
                        MainApplication.applicationContext(),
                        Food::class.java
                    )
                    i.putExtra("id", response)
                    Log.i("Response", response!!)
                    i.flags = FLAG_ACTIVITY_NEW_TASK
                    MainApplication.applicationContext().startActivity(i)
                }
            ) { error -> Log.e("Error", error.toString()) }
        }

        fun customFood(mRequestBody : String) : StringRequest {
            val url = URLs().foodURL
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response: String? ->
                    val i = Intent(
                        MainApplication.applicationContext(),
                        Food::class.java
                    )
                    i.putExtra("id", response)
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

        fun updateFood(mRequestBody: String, id: String) : StringRequest {
            var url = URLs().foodURL
            url += "/$id"
            val stringRequest: StringRequest = object : StringRequest(
                Method.PATCH, url,
                Response.Listener { response: String? ->
                    val i = Intent(
                        MainApplication.applicationContext(),
                        Food::class.java
                    )
                    i.putExtra("id", response)
                    Log.i("Response", response!!)
                    i.flags = FLAG_ACTIVITY_NEW_TASK
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

        private fun ingredientAdapter(foodEntries: JSONArray, ingredientView: RecyclerView, ingredientListener: IngredientListener) {
            var context : Context = MainApplication.applicationContext()

            ingredientView?.adapter = IngredientAdapter(
                context,
                foodEntries,
                ingredientListener
            )
            ingredientView?.layoutManager = LinearLayoutManager(context)
        }

        private fun foodAdapter(foodEntries : String, foodView: RecyclerView, recipe: String, foodListener: FoodListener) {
            var context : Context = MainApplication.applicationContext()


            foodView?.adapter =
                FoodAdapter(context, foodEntries, recipe, foodListener)
            foodView?.layoutManager = LinearLayoutManager(context)
        }

    }
}