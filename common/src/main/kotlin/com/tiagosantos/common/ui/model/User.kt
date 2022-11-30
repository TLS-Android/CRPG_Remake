package com.tiagosantos.common.ui.model

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

data class User(
    val name : String,
    val id: Int,
    val isPrimary: Boolean
    )

interface UserRepository {
    fun findUser(name : String): User?
    fun addUsers(users : List<User>)
}

class UserRepositoryImpl : UserRepository {
    private val _users = arrayListOf<User>()

    override fun findUser(name: String): User? {
        return _users.firstOrNull { it.name == name }
    }

    override fun addUsers(users : List<User>) {
        _users.addAll(users)
    }
}
/*
val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    single { UserService(get()) } //The get() function allow to ask Koin to resolve the needed dependency.
}

class UserApplication : KoinComponent {

    private val userService : UserService by inject()

    // display our data
    fun sayHello(){
        val user = userService.getDefaultUser()
        val message = "Hello '$user'!"
        println(message)
    }
}
*/
//:: creates a member reference or a class reference.