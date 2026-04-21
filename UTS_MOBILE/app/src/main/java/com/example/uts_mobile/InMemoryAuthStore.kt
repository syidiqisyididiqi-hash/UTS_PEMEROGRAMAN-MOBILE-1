package com.example.uts_mobile

object InMemoryAuthStore {
    private val users = mutableMapOf(
        "admin" to "12345"
    )

    fun validateCredentials(username: String, password: String): Boolean {
        return users[username] == password
    }

    fun register(username: String, password: String): Boolean {
        if (users.containsKey(username)) {
            return false
        }
        users[username] = password
        return true
    }
}

