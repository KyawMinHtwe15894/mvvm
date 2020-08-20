package com.example.mvvm.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.mvvm.util.NoInternetExceptions
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(context: Context) : Interceptor {

    private val AppContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()){
            throw NoInternetExceptions("Make sure to have network connection.")
        }
        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable() : Boolean {
        val connectivityManager = AppContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }
}