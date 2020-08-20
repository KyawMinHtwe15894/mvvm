package com.example.mvvm.data.repositories

import com.example.mvvm.data.db.AppDatabase
import com.example.mvvm.data.db.entities.User
import com.example.mvvm.data.network.MyApi
import com.example.mvvm.data.network.SafeApiRequest
import com.example.mvvm.data.network.responses.AuthResponse

class UserRepository(
    private val myApi: MyApi,
    private val db: AppDatabase): SafeApiRequest() {

    suspend fun userLogin(email: String, pw : String) : AuthResponse{
        return apiRequest { myApi.userLogin(email, pw) }
    }

    suspend fun userSignup(name:String, email: String, pw : String) : AuthResponse{
        return apiRequest { myApi.userSignup(name, email, pw) }
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getUser()
}