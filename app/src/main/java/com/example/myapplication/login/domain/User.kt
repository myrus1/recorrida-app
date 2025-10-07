package com.example.myapplication.login.domain

data class User(
    val id: Int,
    val nombre: String,
    val contrasena: String,
    val isAdmin: Boolean = false
)