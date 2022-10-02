package com.tiagosantos.common.ui.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Base UseCase
 *
 * Created on Sep 12, 2022.
 *
 */
abstract class BaseUseCase(
    executionDispatcher: CoroutineDispatcher
) {
    protected val dispatcher = executionDispatcher

    abstract fun logException(e: Exception)
}