package com.macro_manager.macroapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

//  adapted from https://www.simplifiedcoding.net/android-volley-tutorial/
//  singleton for request queues

class VolleySingleton private constructor(private var mCtx: Context) {
    private var mRequestQueue: RequestQueue?

    companion object {
        private var mInstance: VolleySingleton? = null
        @Synchronized
        fun getInstance(context: Context): VolleySingleton? {
            if (mInstance == null) {
                mInstance = VolleySingleton(context)
            }
            return mInstance
        }
    }

    // getApplicationContext() is key, it keeps you from leaking the
    // Activity or BroadcastReceiver if someone passes one in.
    private val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                mRequestQueue = Volley.newRequestQueue(mCtx.applicationContext)
            }
            return mRequestQueue as RequestQueue
        }

    fun <T> addToRequestQueue(req: Request<T>?) {
        requestQueue.add(req)
    }



    init {
        mRequestQueue = requestQueue
    }
}
