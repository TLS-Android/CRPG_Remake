package com.tiagosantos.common.ui.model.impl

import com.tiagosantos.common.ui.model.User
import com.tiagosantos.common.ui.model.api.UserRepository
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl : UserRepository {
    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(userName: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String): Boolean {

        val isDeleteUserSuccessful: Boolean = suspendCoroutine {
        try {
            println("attempt at deleting user")
        }
            catch (ex: Exception){
                println("failed")
            }
        }

        return isDeleteUserSuccessful
    }
}