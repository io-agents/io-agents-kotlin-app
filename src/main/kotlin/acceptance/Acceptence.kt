package com.pawlowski.acceptance

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class Acceptance : IAcceptance {
    private val acceptanceRequest = Channel<String>(Channel.BUFFERED)
    private val acceptanceResponse = Channel<String>(Channel.BUFFERED)

    override suspend fun requestUserAcceptance(llmResult: String): String {
        acceptanceRequest.send(llmResult)
        return acceptanceResponse.receive()
    }

    override fun observeAcceptenceRequests(): Flow<String> = acceptanceRequest.receiveAsFlow()

    override suspend fun handleAcceptence(acceptance: String) {
        acceptanceResponse.send(clarification)
    }
}
