package com.tiagosantos.crpg_remake.domain.usecase

import com.tiagosantos.common.ui.coroutines.BaseCoroutinesUseCase
import com.tiagosantos.common.ui.coroutines.None
import com.tiagosantos.common.ui.model.Meal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import java.util.*

class GetCurrentMealUseCase(
    defaultDispatcher: CoroutineDispatcher,
) : BaseCoroutinesUseCase<Meal, None>(defaultDispatcher) {

    override suspend fun buildUseCase(params: None?): Meal {
        delay(5000)

        return Meal(
            UUID.randomUUID().toString(),
            "Fake Name",
            "ola",
            "ze",
            "ca estamos"
        )
    }

}