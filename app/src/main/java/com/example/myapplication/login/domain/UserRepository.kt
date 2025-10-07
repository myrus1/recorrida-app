package com.example.myapplication.login.domain

interface UserRepository {
    fun login(nombre: String, contrasena: String): LoginResult
    fun logout()
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun isAdmin(): Boolean
    fun agregarUsuario(nombre: String, contrasena: String, isAdmin: Boolean): Boolean
    fun obtenerTodosUsuarios(): List<User>
}
