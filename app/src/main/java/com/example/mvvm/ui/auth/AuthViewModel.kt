package com.example.mvvm.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.mvvm.data.network.NetworkConnectionInterceptor
import com.example.mvvm.data.repositories.UserRepository
import com.example.mvvm.util.ApiExceptions
import com.example.mvvm.util.Corotines
import com.example.mvvm.util.NoInternetExceptions

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var passwordConfirm: String? = null
    var authListener: AuthListener? = null

    fun getLoginUser() = repository.getUser()

    fun onLogin(view: View){
        Intent(view.context, LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            view.context.startActivity(it)
        }
    }

    fun onLoginButtonClick(view: View){
        authListener?.onStarted()
        if(email.isNullOrEmpty() or password.isNullOrEmpty()){
            authListener?.onFailure("Invalid email or password.")
            return
        }

        Corotines.main {
            try {
                val authResponse = repository.userLogin(email!!, password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListener?.onFailure(authResponse.message!!)
            } catch (e: ApiExceptions){
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetExceptions){
                authListener?.onFailure(e.message!!)
            }
        }
    }

    fun onSignup(view: View){
        Intent(view.context, SignupActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            view.context.startActivity(it)
        }
    }

    fun onSignupButtonClick(view: View) {
        authListener?.onStarted()
        if (name.isNullOrEmpty()) {
            authListener?.onFailure("Name is required.")
            return
        }
        if (email.isNullOrEmpty()){
            authListener?.onFailure("Email is required.")
            return
        }
        if(password.isNullOrEmpty()){
            authListener?.onFailure("Password is required.")
            return
        }
        if(password != passwordConfirm){
            authListener?.onFailure("Password didn't match.")
            return
        }

        Corotines.main {
            try {
                val authResponse = repository.userSignup(name!!, email!!, password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListener?.onFailure(authResponse.message!!)
            } catch (e: ApiExceptions){
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetExceptions){
                authListener?.onFailure(e.message!!)
            }
        }
    }
}