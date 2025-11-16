package com.pawlowski.acceptance

import kotlinx.coroutines.flow.Flow

interface IAcceptance {
    suspend fun requestUserAcceptance(llmResult: String): String

    fun observeAcceptenceRequests(): Flow<String>

    suspend fun handleAcceptence(acceptance: String)
}
