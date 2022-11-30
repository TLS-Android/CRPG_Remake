package com.tiagosantos.common.ui.model.api

import com.tiagosantos.common.ui.model.User

interface UserRepository {
    suspend fun getUser(): User

    suspend fun createUser(userName: String): User?

    suspend fun deleteUser(userId: String): Boolean
}