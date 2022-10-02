package com.tiagosantos.common.ui.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Base UseCase with Coroutines
 *
 * Created on Sep 12, 2022.
 *
 */
abstract class BaseCoroutinesUseCase<Results, in Params>(
    executionDispatcher: CoroutineDispatcher
) : BaseUseCase(executionDispatcher) {

    abstract suspend fun buildUseCase(params: Params? = null): Results

    suspend fun execute(params: Params? = null): Result<Results> =
        withContext(dispatcher) {
            try {
                if (dispatcher == Dispatchers.Main)
                    throw RuntimeException("Use case '${this::class.simpleName}' cannot be executed in $dispatcher")

                resultOf {
                    this@BaseCoroutinesUseCase.buildUseCase(params)
                }
            } catch (e: Exception) {
                logException(e)
                Result.failure(Throwable(e.localizedMessage))
            }
        }

    override fun logException(e: Exception) {
        Log.e(this::class.simpleName, "${this::class.simpleName} : ${e.localizedMessage}")
    }
}