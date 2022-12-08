package com.tiagosantos.crpg_remake.domain.usecase

import com.tiagosantos.common.ui.coroutines.BaseCoroutinesFlowUseCase
import com.tiagosantos.common.ui.model.Meal
import com.tiagosantos.crpg_remake.data.repository.MealPublicRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

class GetMealsUseCase(
    defaultDispatcher: CoroutineDispatcher,
    private val repository: MealPublicRepository
) : BaseCoroutinesFlowUseCase<List<String>, String>(defaultDispatcher) {

 /*   override fun buildUseCaseFlow(params: Meal?): Flow<List<String>> =
        params?.toData()?.let { String ->
            repository.getMealsList(user)
                .map(List<String>::toDomain)
        } ?: throw RuntimeException("Unknown chat user")*/

    override fun buildUseCaseFlow(params: String?): Flow<List<String>> {
        TODO("Not yet implemented")
    }


}