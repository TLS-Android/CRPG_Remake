package com.tiagosantos.crpg_remake.data.repository

import com.tiagosantos.common.ui.model.Meal
import kotlinx.coroutines.flow.Flow

interface MealPublicRepository {
    fun getMealsList(meal: Meal): Flow<List<Meal>>
}