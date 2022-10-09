package com.tiagosantos.crpg_remake.domain.interactor

import com.tiagosantos.common.ui.model.Meal
import kotlinx.coroutines.flow.Flow

interface MealPublicInteractor {
    suspend fun getMessagesList(): Flow<Result<List<Meal>>>


}