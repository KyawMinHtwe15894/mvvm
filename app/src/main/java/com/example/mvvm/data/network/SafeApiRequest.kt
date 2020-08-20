package com.example.mvvm.data.network

import com.example.mvvm.util.ApiExceptions
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.StringBuilder

abstract class SafeApiRequest {
    suspend fun<T: Any> apiRequest(call: suspend() -> Response<T>) : T{
        val response = call.invoke()

        if(response.isSuccessful){
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val mesg = StringBuilder()
            error?.let {
                try {
                    mesg.append(JSONObject(it).getString("message"))
                } catch (e: JSONException){ }
                mesg.append("\n")
            }
            mesg.append("Error Code : ${response.code()}")
            throw ApiExceptions(mesg.toString())
        }
    }
}