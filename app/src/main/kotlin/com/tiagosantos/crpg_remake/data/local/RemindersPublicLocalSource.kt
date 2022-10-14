package com.tiagosantos.crpg_remake.data.local

import com.tiagosantos.common.ui.model.Meal
import kotlinx.coroutines.flow.Flow

interface RemindersPublicLocalSource {
    fun getFakeRemindersList(meal: Meal): Flow<List<Meal>>
}