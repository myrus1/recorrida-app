package com.example.myapplication.login.ui

import android.app.admin.TargetUser
import android.provider.ContactsContract
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.intellij.lang.annotations.Pattern

class LoginViewModel : ViewModel(){

    private val _user = MutableLiveData<String>()
    val  user: LiveData <String> = _user

    private val _password = MutableLiveData<String>()
    val  password: LiveData <String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val  loginEnable: LiveData <Boolean> = _loginEnable
    fun onLoginChanged(user: String, password: String) {
        _user.value=user
        _password.value=password
        _loginEnable.value= isValidEmail(user) && isValidPassword(password)
    }

    private fun isValidEmail(user: String): Boolean= Patterns.EMAIL_ADDRESS.matcher(user).matches()
    private fun isValidPassword(password: String): Boolean= password.length>6
    fun onLoginSelected() {

    }

}