package com.pawlowski.clarification

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class ClarificationUseCase : IClarificationUseCase {
    private val clarificationRequest = Channel<String>(Channel.BUFFERED)
    private val clarificationResponse = Channel<String>(Channel.BUFFERED)

    override suspend fun requestUserClarification(llmQuestions: String): String {
        clarificationRequest.send(llmQuestions)
        return clarificationResponse.receive()
    }

    override fun observeClarificationRequests(): Flow<String> = clarificationRequest.receiveAsFlow()

    override suspend fun handleUserClarification(clarification: String) {
        clarificationResponse.send(clarification)
    }
}
