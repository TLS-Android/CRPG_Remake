package com.tiagosantos.crpg_remake.domain.usecase

import com.tiagosantos.crpg_remake.data.repository.ChatPublicRepository
import com.tiagosantos.common.ui.coroutines.BaseCoroutinesFlowUseCase
import com.tiagosantos.common.ui.model.Meal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

class GetPublicChatMessagesUseCase(
    defaultDispatcher: CoroutineDispatcher,
    private val repository: ChatPublicRepository
) : BaseCoroutinesFlowUseCase<List<String>, String>(defaultDispatcher) {

    override fun buildUseCaseFlow(params: Meal?): Flow<List<String>> =
        params?.toData()?.let { String ->
            repository.getMessagesList(user)
                .map(List<ChatMessageData>::toDomain)
        } ?: throw RuntimeException("Unknown chat user")


}