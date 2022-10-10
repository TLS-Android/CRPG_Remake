package com.tiagosantos.crpg_remake.data.repository

import com.tiagosantos.common.ui.model.Meal
import com.tiagosantos.crpg_remake.data.local.MealPublicLocalSource
import com.tiagosantos.crpg_remake.domain.usecase.GetMealsUseCase
import kotlinx.coroutines.flow.Flow

class ImplMealPublicRepository(
    private val localSource: MealPublicLocalSource
) : MealPublicRepository {

    override fun getMealsList(meal: Meal): Flow<List<Meal>> {
        getCurrentUserUseCase.execute()
            .andThenFlow(GetMealsUseCase::execute)
    }
}