package Requests

import Adapters.DiaryRecipeAdapter
import Adapters.IngredientAdapter
import Adapters.RecipeAdapter
import Listeners.DiaryRecipeListener
import Listeners.IngredientListener
import Listeners.RecipeListener
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.macro_manager.macroapp.*
import com.macro_manager.macroapp.Food
import com.macro_manager.macroapp.Recipe
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class Recipe {

    companion object {
        fun getRequest(recipeView : RecyclerView, category: String, recipeListener: RecipeListener) : StringRequest {

            val url = URLs().recipeURL
            val stringRequest =  StringRequest( Request.Method.GET, url, { response ->
                Log.i("Response", response.toString())
                recipeAdapter(response, recipeView, category, recipeListener )
            },
                { Log.e("Error", "Error retrieving response") })
            return stringRequest
        }

        fun getListedRecipes(ingredientView: RecyclerView, recipeListener: DiaryRecipeListener, recipeIds : ArrayList<Int>): JsonArrayRequest {
            val jsonArray = JSONArray()
            val foodArray = JSONArray()
            for (i in recipeIds) {
                foodArray.put(i)
            }
            val jsonObject = JSONObject().put("IDs", foodArray)
            jsonArray.put(jsonObject)
            val url = URLs().recipeURL + "/recipes"
            val stringRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.POST, url, jsonArray,
                Response.Listener { response: JSONArray ->
                    diaryRecipeAdapter(response, ingredientView, recipeListener)
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

        fun getIngredientsRequest(ingredientView: RecyclerView, id: String, ingredientListener: IngredientListener) : StringRequest {
            val url = URLs().recipeURL + "/ingredients/$id"
            val stringRequest = StringRequest( Request.Method.GET, url, { response ->
                Log.i("Response", response.toString())
                val jsonArray = JSONArray(response.toString())

                ingredientAdapter(jsonArray, ingredientView, ingredientListener)
            },
            { Log.e("Error", "Error retrieving response") })
            return stringRequest
        }

        fun deleteRequest(id: String) : StringRequest {
            var url = URLs().recipeURL
            url += "/$id"
            return StringRequest(
                Request.Method.DELETE, url,
                { response: String? ->
                    val i = Intent(
                        MainApplication.applicationContext(),
                        Food::class.java
                    )
                    i.putExtra("Category", "Recipe")
                    i.putExtra("id", response)
                    Log.i("Response", response!!)
                    MainApplication.applicationContext().startActivity(i)
                }
            ) { error -> Log.e("Error", error.toString()) }
        }

        fun updateRequest(id: String, mRequestBody : String) : StringRequest {
            var url = URLs().recipeURL
            url += "/details/$id"
            println(mRequestBody)
            val stringRequest : StringRequest = object : StringRequest(
                Method.PATCH, url,
                Response.Listener { response: String? ->
                    val i = Intent(
                        MainApplication.applicationContext(),
                        Recipe::class.java
                    )
                    i.putExtra("Category", "Recipe")
                    i.putExtra("id", response)
                    i.flags = FLAG_ACTIVITY_NEW_TASK
                    Log.i("Response", response!!)
                    MainApplication.applicationContext().startActivity(i)
                }, Response.ErrorListener { error -> Log.e("Error", error.toString()) }) {
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

        fun updateIngredientsRequest(mRequestBody: String) : StringRequest {
            var url = URLs().recipeURL
            url += "/ingredients"
            println(mRequestBody)
            val stringRequest : StringRequest = object : StringRequest(
                Method.PATCH, url,
                Response.Listener { response: String? ->
                    val i = Intent(
                        MainApplication.applicationContext(),
                        Recipe::class.java
                    )
                    i.putExtra("Category", "Recipe")
                    i.putExtra("id", response)
                    i.flags = FLAG_ACTIVITY_NEW_TASK
                    Log.i("Response", response!!)
                    MainApplication.applicationContext().startActivity(i)
                }, Response.ErrorListener { error -> Log.e("Error", error.toString()) }) {
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

        fun createRequest(mRequestBody : String): StringRequest {
            val url = URLs().recipeURL
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response: String? ->
                    val i = Intent(
                        MainApplication.applicationContext(),
                        Recipe::class.java
                    )
                    i.flags = FLAG_ACTIVITY_NEW_TASK
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

        private fun ingredientAdapter(foodEntries: JSONArray, ingredientView: RecyclerView, ingredientListener: IngredientListener) {
            var context : Context = MainApplication.applicationContext()

            ingredientView?.adapter = IngredientAdapter(
                context,
                foodEntries,
                ingredientListener
            )
            ingredientView?.layoutManager = LinearLayoutManager(context)
        }

        private fun recipeAdapter(recipeEntries : String, recipeView : RecyclerView, category: String, recipeListener: RecipeListener) {
            var context : Context = MainApplication.applicationContext()
            recipeView.adapter = RecipeAdapter(context, recipeEntries, category, recipeListener)
            recipeView.layoutManager = LinearLayoutManager(context)
        }

        private fun diaryRecipeAdapter(recipeEntries: JSONArray, recipeView: RecyclerView, diaryRecipeListener: DiaryRecipeListener) {
            var context : Context = MainApplication.applicationContext()
            recipeView.adapter = DiaryRecipeAdapter(context, recipeEntries, diaryRecipeListener)
        }
    }

}