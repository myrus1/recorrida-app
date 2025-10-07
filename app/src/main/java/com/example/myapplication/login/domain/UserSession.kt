package com.example.myapplication.login.domain

data class UserSession(
    val user: User,
    val isLoggedIn: Boolean = true
)