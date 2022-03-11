package Requests

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import Adapters.DiaryAdapter
import com.macro_manager.macroapp.MainApplication

class Diary {
    companion object {
        fun getDiaries(diaryView: RecyclerView) : StringRequest{
            val url = URLs().diaryURL
            return StringRequest( Request.Method.GET, url, { response ->
                Log.i("Response", response.toString())
                adapter(response, diaryView)},
                { Log.e("Error", "Error retrieving response") })
        }


        private fun adapter(diaryEntries : String, diaryView : RecyclerView) {
            val context = MainApplication.applicationContext()
            diaryView?.adapter = DiaryAdapter(context, diaryEntries)
            diaryView?.layoutManager = LinearLayoutManager(context)
        }
    }
}