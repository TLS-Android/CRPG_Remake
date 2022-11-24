package com.tiagosantos.crpg_remake.domain.interactor

import com.tiagosantos.common.ui.coroutines.andThenFlow
import com.tiagosantos.crpg_remake.domain.usecase.GetCurrentUserUseCase
import com.tiagosantos.crpg_remake.domain.usecase.GetMealsUseCase
import com.tiagosantos.common.ui.model.Meal
import kotlinx.coroutines.flow.Flow

/**
 * Interactor uses in Public chat View Model
 *
 * Created on Dec 15, 2020.
 *
 */
class ImplMealPublicInteractor(
    private val GetMealsUseCase: GetMealsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : MealPublicInteractor {

    override suspend fun getMessagesList(): Flow<Result<List<Meal>>> =
        getCurrentUserUseCase.execute()
            .andThenFlow(getPublicChatMessagesUseCase::execute)
}