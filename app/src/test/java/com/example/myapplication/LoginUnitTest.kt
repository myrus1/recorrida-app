// test/kotlin/com/example/myapplication/login/domain/UserRepositoryImplTest.kt
package com.example.myapplication.login.domain

import io.mockk.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl
    // Si usas SharedPreferences, Room, etc, mockealo aquí
    private val mockDataSource: UserDataSource = mockk(relaxed = true)

    @Before
    fun setup() {
        // Inicializar el repository antes de cada test
        repository = UserRepositoryImpl(mockDataSource)
    }

    @After
    fun tearDown() {
        // Limpiar mocks después de cada test
        clearAllMocks()
    }

    // ========== TESTS DE LOGIN ==========

    @Test
    fun `login con credenciales validas retorna Success`() {
        // Given
        val nombre = "admin"
        val contrasena = "1234"
        val expectedUser = User(nombre, isAdmin = true)
        every { mockDataSource.findUser(nombre, contrasena) } returns expectedUser

        // When
        val result = repository.login(nombre, contrasena)

        // Then
        assertTrue(result is LoginResult.Success)
        assertEquals(expectedUser, (result as LoginResult.Success).user)
        verify { mockDataSource.findUser(nombre, contrasena) }
    }

    @Test
    fun `login con credenciales invalidas retorna Error`() {
        // Given
        every { mockDataSource.findUser(any(), any()) } returns null

        // When
        val result = repository.login("wrong", "wrong")

        // Then
        assertTrue(result is LoginResult.Error)
    }

    @Test
    fun `login con nombre vacio retorna Error`() {
        // When
        val result = repository.login("", "1234")

        // Then
        assertTrue(result is LoginResult.Error)
        assertEquals("El nombre no puede estar vacío", (result as LoginResult.Error).message)
    }

    @Test
    fun `login con contrasena vacia retorna Error`() {
        // When
        val result = repository.login("admin", "")

        // Then
        assertTrue(result is LoginResult.Error)
        assertEquals("La contraseña no puede estar vacía", (result as LoginResult.Error).message)
    }

    @Test
    fun `login guarda usuario actual en sesion`() {
        // Given
        val user = User("admin", isAdmin = true)
        every { mockDataSource.findUser(any(), any()) } returns user

        // When
        repository.login("admin", "1234")

        // Then
        assertEquals(user, repository.getCurrentUser())
    }

    // ========== TESTS DE LOGOUT ==========

    @Test
    fun `logout limpia usuario actual`() {
        // Given - primero hacer login
        val user = User("admin", isAdmin = true)
        every { mockDataSource.findUser(any(), any()) } returns user
        repository.login("admin", "1234")

        // When
        repository.logout()

        // Then
        assertNull(repository.getCurrentUser())
        assertFalse(repository.isLoggedIn())
    }

    @Test
    fun `logout limpia datos de sesion`() {
        // Given
        every { mockDataSource.findUser(any(), any()) } returns User("user", false)
        repository.login("user", "pass")

        // When
        repository.logout()

        // Then
        verify { mockDataSource.clearSession() }
    }

    // ========== TESTS DE getCurrentUser ==========

    @Test
    fun `getCurrentUser retorna null cuando no hay sesion`() {
        // When
        val user = repository.getCurrentUser()

        // Then
        assertNull(user)
    }

    @Test
    fun `getCurrentUser retorna usuario despues de login`() {
        // Given
        val expectedUser = User("testuser", isAdmin = false)
        every { mockDataSource.findUser(any(), any()) } returns expectedUser
        repository.login("testuser", "pass")

        // When
        val user = repository.getCurrentUser()

        // Then
        assertEquals(expectedUser, user)
    }

    // ========== TESTS DE isLoggedIn ==========

    @Test
    fun `isLoggedIn retorna false inicialmente`() {
        // When
        val result = repository.isLoggedIn()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isLoggedIn retorna true despues de login exitoso`() {
        // Given
        every { mockDataSource.findUser(any(), any()) } returns User("user", false)
        repository.login("user", "pass")

        // When
        val result = repository.isLoggedIn()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isLoggedIn retorna false despues de logout`() {
        // Given
        every { mockDataSource.findUser(any(), any()) } returns User("user", false)
        repository.login("user", "pass")
        repository.logout()

        // When
        val result = repository.isLoggedIn()

        // Then
        assertFalse(result)
    }

    // ========== TESTS DE isAdmin ==========

    @Test
    fun `isAdmin retorna false cuando no hay usuario logueado`() {
        // When
        val result = repository.isAdmin()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isAdmin retorna true cuando usuario es admin`() {
        // Given
        every { mockDataSource.findUser(any(), any()) } returns User("admin", isAdmin = true)
        repository.login("admin", "pass")

        // When
        val result = repository.isAdmin()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isAdmin retorna false cuando usuario no es admin`() {
        // Given
        every { mockDataSource.findUser(any(), any()) } returns User("user", isAdmin = false)
        repository.login("user", "pass")

        // When
        val result = repository.isAdmin()

        // Then
        assertFalse(result)
    }

    // ========== TESTS DE agregarUsuario ==========

    @Test
    fun `agregarUsuario con datos validos retorna true`() {
        // Given
        every { mockDataSource.userExists("newuser") } returns false
        every { mockDataSource.insertUser(any()) } returns true

        // When
        val result = repository.agregarUsuario("newuser", "pass123", isAdmin = false)

        // Then
        assertTrue(result)
        verify { mockDataSource.insertUser(match { it.nombre == "newuser" }) }
    }

    @Test
    fun `agregarUsuario con nombre existente retorna false`() {
        // Given
        every { mockDataSource.userExists("admin") } returns true

        // When
        val result = repository.agregarUsuario("admin", "pass", false)

        // Then
        assertFalse(result)
        verify(exactly = 0) { mockDataSource.insertUser(any()) }
    }

    @Test
    fun `agregarUsuario con nombre vacio retorna false`() {
        // When
        val result = repository.agregarUsuario("", "pass", false)

        // Then
        assertFalse(result)
    }

    @Test
    fun `agregarUsuario con contrasena vacia retorna false`() {
        // When
        val result = repository.agregarUsuario("user", "", false)

        // Then
        assertFalse(result)
    }

    @Test
    fun `agregarUsuario con contrasena corta retorna false`() {
        // When (asumiendo mínimo 4 caracteres)
        val result = repository.agregarUsuario("user", "123", false)

        // Then
        assertFalse(result)
    }

    @Test
    fun `agregarUsuario solo puede ser llamado por admin`() {
        // Given - usuario normal logueado
        every { mockDataSource.findUser(any(), any()) } returns User("user", isAdmin = false)
        repository.login("user", "pass")

        // When
        val result = repository.agregarUsuario("newuser", "pass", false)

        // Then
        assertFalse(result)
        // O lanzar excepción según tu lógica
    }

    // ========== TESTS DE obtenerTodosUsuarios ==========

    @Test
    fun `obtenerTodosUsuarios retorna lista vacia cuando no hay usuarios`() {
        // Given
        every { mockDataSource.getAllUsers() } returns emptyList()

        // When
        val result = repository.obtenerTodosUsuarios()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `obtenerTodosUsuarios retorna todos los usuarios`() {
        // Given
        val users = listOf(
            User("admin", isAdmin = true),
            User("user1", isAdmin = false),
            User("user2", isAdmin = false)
        )
        every { mockDataSource.getAllUsers() } returns users

        // When
        val result = repository.obtenerTodosUsuarios()

        // Then
        assertEquals(3, result.size)
        assertEquals(users, result)
    }

    @Test
    fun `obtenerTodosUsuarios solo puede ser llamado por admin`() {
        // Given - usuario normal logueado
        every { mockDataSource.findUser(any(), any()) } returns User("user", isAdmin = false)
        repository.login("user", "pass")

        // When
        val result = repository.obtenerTodosUsuarios()

        // Then
        assertTrue(result.isEmpty())
        // O lanzar excepción según tu implementación
    }

    @Test
    fun `obtenerTodosUsuarios no incluye contrasenas en la respuesta`() {
        // Given
        val users = listOf(User("admin", isAdmin = true))
        every { mockDataSource.getAllUsers() } returns users

        // When
        val result = repository.obtenerTodosUsuarios()

        // Then
        // Verificar que las contraseñas no se exponen
        result.forEach { user ->
            // Si User tiene campo password, verificar que esté vacío/null
            assertNull(user.contrasena) // o assertEquals("", user.contrasena)
        }
    }
}