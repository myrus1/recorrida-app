package com.example.myapplication.login.data
import com.example.myapplication.login.domain.User
import com.example.myapplication.login.domain.UserSession
import com.example.myapplication.login.domain.LoginResult
import com.example.myapplication.login.domain.UserRepository

object UserManager : UserRepository {
    private val usuarios = mutableListOf<User>()
    private var currentSession: UserSession? = null

    init {
        // Usuario admin por defecto
        usuarios.add(User(1, "admin", "admin123", isAdmin = true))
        // Usuarios de ejemplo
        usuarios.add(User(2, "Facu", "pass123", isAdmin = false))
        usuarios.add(User(3, "operador", "pass456", isAdmin = false))
    }

    override fun login(nombre: String, contrasena: String): LoginResult {
        val user = usuarios.find {
            it.nombre.equals(nombre, ignoreCase = true) &&
                    it.contrasena == contrasena
        }

        return if (user != null) {
            currentSession = UserSession(user)
            LoginResult.Success(user)
        } else {
            LoginResult.Error("Usuario o contraseña incorrectos")
        }
    }

    override fun logout() {
        currentSession = null
    }

    override fun getCurrentUser(): User? = currentSession?.user

    override fun isLoggedIn(): Boolean = currentSession != null

    override fun isAdmin(): Boolean = currentSession?.user?.isAdmin ?: false

    override fun agregarUsuario(nombre: String, contrasena: String, isAdmin: Boolean): Boolean {
        if (!isAdmin()) return false

        if (usuarios.any { it.nombre.equals(nombre, ignoreCase = true) }) {
            return false
        }

        val nuevoId = (usuarios.maxOfOrNull { it.id } ?: 0) + 1
        usuarios.add(User(nuevoId, nombre, contrasena, isAdmin))
        return true
    }

    override fun obtenerTodosUsuarios(): List<User> {
        return if (isAdmin()) usuarios.toList() else emptyList()
    }
}